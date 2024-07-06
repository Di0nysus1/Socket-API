package de.dion.socket;

import java.awt.List;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import de.dion.socket.localobjects.enums.ConnectionAction;
import de.dion.socket.localobjects.enums.ServerState;
import de.dion.socket.localobjects.user.User;
import de.dion.socket.localobjects.user.UserHandle;
import de.dion.socket.logger.LogLevel;
import de.dion.socket.main.Main;
import de.dion.socket.utils.ColorUtils;
import de.dion.socket.utils.MathHelper;

public class UserManager {
	
	private volatile LinkedList<User> users;
	private volatile LinkedList<UserHandle> handles;
	private Thread thread = null;
	
	/**
	 * Um den UserManager zu starten einfach nur diesen Constructor aufrufen
	 * */
	public UserManager()
	{
		users = new LinkedList<>();
		handles = new LinkedList<>();
		handles.clear();
		start();
	}
	
	/**
	 * #Multifredding
	 * Startet den UserManager
	 */
	private void start()
	{
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(Server.serverstate != ServerState.STOPPING)
				{
					try {
						Server.debug("loop start");
						onUserCheck();
						Server.debug("loop end");
						Thread.sleep(500);
					} catch (Exception e) {
						if(Main.debugmode)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}, "UserManagerCheckThread");
		thread.start();
	}
	
	/**
	 * Returnt true wenn es in der Collection einen User mit der Selben HWID wie dem User gibt
	 * */
	private boolean isLocalOnline(Collection<User> localusers, User u)
	{
		String HWID = u.getHWID();
		for(User lu: localusers)
		{
			if(lu.getHWID().equals(HWID))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returnt true wenn es in der Collection einen User mit dem selben Socket wie dem User gibt
	 * */
	private boolean isAnySocketEqual(Collection<User> localusers, User u)
	{
		for(User lu: localusers)
		{
			if(lu.getLoginSocket().equals(u.getLoginSocket()) || lu.getLoginSocket() == u.getLoginSocket())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Das Event added und removed User
	 * */
	private synchronized void onUserCheck()
	{
		ArrayList<UserHandle> localhandles = new ArrayList<>();
		synchronized (handles) {
		
			if(handles.isEmpty())
			{
				Server.debug("handles empty");
				checkTimoutedUsers();
				return;
			}
			
			while(!handles.isEmpty())
			{
				if(handles.isEmpty())
				{
					break;
				}
				localhandles.add(handles.getFirst());
				handles.removeFirst();
			}
		}
		LinkedList<User> localusers = getUsers();
		ArrayList<String> removed = new ArrayList<>();
		ArrayList<String> added = new ArrayList<>();
		
		for(UserHandle userhandle: localhandles)
		{
			if(userhandle.getAction() == ConnectionAction.PING) //ping refresh
			{
				for(User u: this.users)
				{
					if(u.getHWID().equals(userhandle.getHWID()))
					{
						Server.debug("Ping packet kam an von " + u.getHWID());
						u.refreshPing();
					}
				}
				userhandle.getUser().refreshPing();
			}
			else if(userhandle.getAction() == ConnectionAction.LEAVE) //leave
			{
				User u = userhandle.getUser();
				
				
				if(isLocalOnline(localusers, u) && isAnySocketEqual(localusers, u))
				{
					if(added.contains(u.getHWID()))
					{
						added.remove(u.getHWID());
					}
					else if(!removed.contains(u.getHWID()))
					{
						removed.add(u.getHWID());
					}
					try {
						u.getLoginSocket().close();
					} catch (Exception e) {}
					try {
						for(User lu: localusers)
						{
							if(lu.getHWID().equals(u.getHWID()))
							{
								lu.getLoginSocket().close();
							}
						}
					} catch (Exception e) {}
					for(int i = localusers.size() - 1; i >= 0; i--)
					{
						if(localusers.get(i).getHWID().equals(u.getHWID()))
						{
							localusers.remove(i);
						}
					}
				}
			}
			else if(userhandle.getAction() == ConnectionAction.JOIN) //join
			{
				User u = userhandle.getUser();
				
				if(isLocalOnline(localusers, u)) //ist bereits online
				{
					if(isAnySocketEqual(localusers, u)) //nix machen sonst nicht gut
					{
						if(removed.contains(u.getHWID()))
						{
							removed.remove(u.getHWID());
						}
					}
					else // user wird ersetzt
					{
						if(removed.contains(u.getHWID()))
						{
							removed.remove(u.getHWID());
						}
						
						try {
							for(User lu: localusers)
							{
								if(lu.getHWID().equals(u.getHWID()))
								{
									lu.getLoginSocket().close();
								}
							}
						} catch (IOException e) {}
						
						for(int i = localusers.size() - 1; i >= 0; i--)
						{
							if(localusers.get(i).getHWID().equals(u.getHWID()))
							{
								localusers.remove(i);
							}
						}
						localusers.add(u);
					}
				}
				else //ist nicht online
				{
					if(removed.contains(u.getHWID()))
					{
						removed.remove(u.getHWID());
					}
					else
					{
						added.add(u.getHWID());
					}
					localusers.add(u);
				}
			}
		}
		this.users = localusers;
		
		for(String s: added)
		{
			Server.log("[+] " + s + " [+]");
			System.out.println("[" + ColorUtils.GREEN + "+" + ColorUtils.RESET + "] " +  ColorUtils.BLUE_BRIGHT + s + ColorUtils.RESET + " [" + ColorUtils.GREEN + "+" + ColorUtils.RESET + "]");
			User u = getUser(s);
			if(u != null)
			{
				Server.usercache.log("[+] " + s + " --> " + u.getRAWID() + u.getIPAddress(), LogLevel.INFORMATION);
			}
		}
		
		for(String s: removed)
		{
			Server.log("[-] " + s + " [-]");
			System.out.println("[" + ColorUtils.RED + "-" + ColorUtils.RESET + "] " +  ColorUtils.BLUE_BRIGHT + s + ColorUtils.RESET + " [" + ColorUtils.RED + "-" + ColorUtils.RESET + "]");
			Server.usercache.log("[-] " + s, LogLevel.INFORMATION);
		}
		
		
		checkTimoutedUsers();
	}
	
	/**
	 * Checkt das Delay, wann das letzte Ping packet der User an kam
	 * */
	private void checkTimoutedUsers() {
		Server.debug("checking timeout...");
		for(User u: getUsers())
		{
			Server.debug("delay: " + u.getDelay() + "");
			if(u.isTimedOut() || !u.getLoginSocket().isConnected())
			{
				if(Main.debugmode) {
					if(u.isTimedOut()) {
						Server.debug("time out");
					} if(!u.getLoginSocket().isConnected()) {
						Server.debug("is nicht mehr connected");
					}  if(u.getLoginSocket().isClosed()) {
						Server.debug("is closed");
					}
				}
				addHandle(u, ConnectionAction.LEAVE);
			}
		}
	}
	
	/**
	 * Return true wenn ein User mit der HWID online ist
	 * */
	public boolean isOnline(String HWID)
	{
		LinkedList<User> localusers = getUsers();
		for(User u: localusers)
		{
			if(u.getHWID().equals(HWID))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return true wenn ein User die selbe HWID hat
	 * */
	public boolean isOnline(User u)
	{
		LinkedList<User> localusers = getUsers();
		String HWID = u.getHWID();
		for(User lu: localusers)
		{
			if(lu.getHWID().equals(HWID))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returnt eine Kopie der Userliste
	 * */
	public LinkedList<User> getUsers()
	{
		return (LinkedList<User>)users.clone();
	}
	
	/**
	 * Returnt den kopierten User
	 * */
	public User getUser(String HWID)
	{
		LinkedList<User> userlist = getUsers();
		for(User u: userlist)
		{
			if(u.getHWID().equals(HWID))
			{
				return u;
			}
		}
		return null;
	}
	
	/**
	 * Returnt den kopierten User
	 * */
	public User getUser(Socket so)
	{
		LinkedList<User> userlist = getUsers();
		for(User u: userlist)
		{
			if(u.getLoginSocket().equals(so))
			{
				return u;
			}
		}
		return null;
	}
	
	/**
	 * Hier kann man dem Usermanager Sagen dass ein User geadded oder removed werden soll
	 * */
	public synchronized void addHandle(User u, ConnectionAction a)
	{
		if(a == null)
		{
			throw new NullPointerException("Die ConnectionAction darf nicht null sein!");
		}
		UserHandle handle = new UserHandle(u, a);
		this.handles.add(handle);
	}
	
	/**
	 * Bitte nur zum leaven und pingen benutzen
	 * */
	public synchronized void addHandle(String HWID, ConnectionAction a)
	{
		if(a == ConnectionAction.JOIN)
		{
			throw new IllegalStateException("Beim joinen bitte den User angeben!");
		}
		UserHandle handle = new UserHandle(getUser(HWID), a);
		this.handles.add(handle);
	}
	
	/**
	 * Diese Methode sagt mit wie vielen Clients eine HWID Addresse bereits online ist.
	 * Siehe socket.options.Options Zeile 15
	 * */
	public int getConnections(InetAddress ip)
	{
		int count = 0;
		LinkedList<User> localusers = getUsers();
		for(User u: localusers)
		{
			if(u.getLoginSocket().getInetAddress().getHostAddress().equals(ip.getHostAddress()))
			{
				count++;
			}
		}
		return count;
	}
	
}
