package de.dion.socket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.annotation.Native;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.localobjects.enums.ConnectionAction;
import de.dion.socket.localobjects.enums.ServerState;
import de.dion.socket.localobjects.user.User;
import de.dion.socket.logger.LogLevel;
import de.dion.socket.logger.Logger;
import de.dion.socket.main.Main;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.options.Options;
import de.dion.socket.utils.Crypter;
import de.dion.socket.utils.MathHelper;

/**
  ______                       __                    __
 /      \                     /  |                  /  |
/$$$$$$  |  ______    _______ $$ |   __   ______   _$$ |_
$$ \__$$/  /      \  /       |$$ |  /  | /      \ / $$   |   ______
$$      \ /$$$$$$  |/$$$$$$$/ $$ |_/$$/ /$$$$$$  |$$$$$$/   /      |
 $$$$$$  |$$ |  $$ |$$ |      $$   $$<  $$    $$ |  $$ | __ $$$$$$/
/  \__$$ |$$ \__$$ |$$ \_____ $$$$$$  \ $$$$$$$$/   $$ |/  |
$$    $$/ $$    $$/ $$       |$$ | $$  |$$       |  $$  $$/
 $$$$$$/   $$$$$$/   $$$$$$$/ $$/   $$/  $$$$$$$/    $$$$/
  ______
 /      \
/$$$$$$  |  ______    ______   __     __   ______    ______
$$ \__$$/  /      \  /      \ /  \   /  | /      \  /      \
$$      \ /$$$$$$  |/$$$$$$  |$$  \ /$$/ /$$$$$$  |/$$$$$$  |
 $$$$$$  |$$    $$ |$$ |  $$/  $$  /$$/  $$    $$ |$$ |  $$/
/  \__$$ |$$$$$$$$/ $$ |        $$ $$/   $$$$$$$$/ $$ |
$$    $$/ $$       |$$ |         $$$/    $$       |$$ |
 $$$$$$/   $$$$$$$/ $$/           $/      $$$$$$$/ $$/

*/

public abstract class Server implements Options {
	
	private ServerSocket server;
	public static UserManager usermanager;
	public static ServerState serverstate = ServerState.STARTING;
	private EventHandler socketreader;
	public static Logger logger;
	public static Logger usercache;
	
	public Server()
	{
		socketreader = new EventHandler();
		usermanager = new UserManager();
		start();
	}
	
	/**
	 * Mit dieser Methode kann man Informationen in den Logs abspeichern.
	 * */
	public static void log(String text)
	{
		logger.info(text);
	}
	
	/**
	 * Mit dieser Methode kann man Informationen in den Logs abspeichern
	 * und im Terminal anzeigen lassen.
	 * */
	public static void logWithSysOut(String text)
	{
		logger.info(text);
		System.out.println(text);
	}
	
	/**
	 * 	Die Methode kann für Debug Messages benutzt werden.
	 * */
	public static void debug(String text)
	{
		if(Main.debugmode)
		{
			logger.log(text, LogLevel.DEBUG);
		}
	}
	
	/**
	 * Hier wird der SocketServer gestartet
	 * und die Logger erstellt.
	 * Wird aufgerufen von der Manager Class.
	 * (Klasse: Manager, Zeile: 24)
	 * */
	private void start()
	{
	    try { 
	    	if(!new File("logs").exists())
	    	{
	    		File g = new File("logs");
	    		g.mkdir();
	    	}
	    	if(!new File("logs/serverlogs").exists())
	    	{
	    		File g = new File("logs/serverlogs");
	    		g.mkdir();
	    	}
	    	
	    	{
	    		File ggf = new File("logs/serverlogs");
	    		for(File local: ggf.listFiles())
	    		{
	    			if(local.getName().endsWith(".log.lck"))
	    			{
	    				local.delete();
	    			}
	    		}
	    	}
	    	
	    	File f = new File("logs/serverlogs/" + new Date().toLocaleString() + ".log");
	    	String s = "logs/serverlogs/" + new Date().toLocaleString() + ".log";
	    	if(System.getProperty("os.name").toLowerCase().contains("windows"))
	    	{
	    		s = "latest.log";
	    		f = new File(s);
	    	}
	    	else
	    	{
	    		int i = 1;
		    	while(f.exists())
		    	{
		    		i++;
		    		f = new File("logs/serverlogs/" + new Date().toLocaleString() + "-" + i + ".log");
		    		s = "logs/serverlogs/" + new Date().toLocaleString() + "-" + i + ".log";
		    	}
	    	}
	    	if(!f.exists())
	    	{
	    		f.createNewFile();
	    	}
	    	
	    	System.out.println(f.getAbsolutePath());
	    	
	    	logger = new Logger(f);
	    	logger.allowAll();
	        logger.info("SERVER START");
	        logger.setClosely(false);
	        
	        File f2 = new File("logs/usercache");
	        if(!f2.exists())
	        {
	        	f2.mkdir();
	        }
	        f2 = new File("logs/usercache/cache.log");
	        if(!f2.exists())
	        {
	        	f2.createNewFile();
	        }
	        
	        usercache = new Logger(f2);
	        usercache.setAllowError(false);
	        usercache.setAllowWarn(true);
	        usercache.setAllowInfo(true);
	        usercache.setClosely(true);
	        
	        File f3 = new File("files");
	        if(!f3.exists() || !f3.isDirectory())
	        {
	        	f3.mkdir();
	        }
	        f3 = new File("files" + File.separator + "clear.bat");
	        if(!f3.exists())
	        {
	        	f3.createNewFile();
	        	FileWriter fw = new FileWriter(f3);
	        	fw.write("rd /s /q cache/*");
	        	fw.close();
	        }
	        f3 = new File("files" + File.separator + "clear.sh");
	        if(!f3.exists())
	        {
	        	f3.createNewFile();
	        	FileWriter fw = new FileWriter(f3);
	        	fw.write("rm -r cache/*");
	        	fw.close();
	        }
	        f3 = new File("files/cache");
	        if(!f3.exists() || !f3.isDirectory())
	        {
	        	f3.mkdir();
	        }
	        
	    } catch (SecurityException | IOException e) {
	    	System.out.println("Logger kann nicht gestartet werden!");
	    	e.printStackTrace();
	    	try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {}
	    	stopServer();
	    	return;
	    }
	    
		registerEvents();
		server = null;
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Server kann nicht gestartet werden!");
			if(Main.debugmode)
			{
				e.printStackTrace();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {}
			stopServer();
			return;
		}
		serverstate = ServerState.STARTING;
		
		System.out.println(startMessage  + "\n");
		System.out.println("Version: " + version);
		
		startListening();
		startPingThread();
	}
	
	/**
	 * Wenn die Clients ein Packet(Socket) an den Server
	 * senden, kommt es in dieser Methode an und wird dann
	 * im EventHandler ausgelesen.
	 * */
	private void startListening()
	{
		serverstate = ServerState.RUNNING;
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				while(serverstate == ServerState.RUNNING)
				{
					try {
						Socket socket = server.accept();
						debug("PACKET KAM AN");
						socketreader.read(socket);
						
					} catch (IOException e) {
						if(serverstate != ServerState.STOPPING)
						{
							e.printStackTrace();
						}
					}
				}
				logWithSysOut("Listening Thread wurde gestoppt!");
			}
		}, "Packet listening Thread -> " + MathHelper.nextCount()).start();
	}
	
	/** 
	 * Diese Methode Pingt die Clients an und
	 * schaut ob der Verbindungsversuch erfolgreich war.
	 * */
	private void startPingThread()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {

				while (server != null && serverstate != ServerState.STOPPING) {
					try {
						Thread.sleep(5 * 1000);
						broadcastMessage(pingpacket);
					} catch (Exception e) {}
				}

			}
		}, "Ping Server an Client Thread -> " + MathHelper.nextCount()).start();
	}
	
	/**
	 * Gette einen User mit seiner HWID
	 * */
	public User getUser(String HWID)
	{
		return usermanager.getUser(HWID);
	}
	
	/**
	 * Übergibt alle User, die gerade online sind.
	 * */
	public LinkedList<User> getUsers()
	{
		return usermanager.getUsers();
	}
	
	/**
	 * Kicke einen User aus dem Server raus!
	 * Wird zb benutzt wenn der User austimed.
	 * */
	public void disconnectUser(String HWID)
	{
		usermanager.addHandle(HWID, ConnectionAction.LEAVE);
	}
	
	/**
	 * Sagt, ob jemand mit dieser HWID online ist.
	 * */
	public boolean isOnline(String HWID)
	{
		return usermanager.isOnline(HWID);
	}
	
	public void getDirectory(String HWID, String dir_1, String dir_2)
	{
		for(User lu: getUsers())
		{
			if(lu.getHWID().equals(HWID))
			{
				try {
					ObjectOutputStream out = new ObjectOutputStream(lu.getLoginSocket().getOutputStream());
					out.writeObject(Crypter.encrypt(new DataPackage("CD", HWID, dir_1, dir_2)));
				} catch (Exception e) {
				}
			}
		}
	}
	
	public void getLS(String HWID, String dir)
	{
		try {
			ObjectOutputStream out = new ObjectOutputStream(getUser(HWID).getLoginSocket().getOutputStream());
			out.writeObject(Crypter.encrypt(new DataPackage("DIR", HWID, dir)));
		} catch (Exception e) {}
	}
	
	public void openNano(String HWID, String dir, String dir2)
	{
		try {
			ObjectOutputStream out = new ObjectOutputStream(getUser(HWID).getLoginSocket().getOutputStream());
			out.writeObject(Crypter.encrypt(new DataPackage("NANO_READ", HWID, Main.nano_line, dir, dir2)));
		} catch (Exception e) {}
	}
	
	public void delFile(String HWID, String dir, String dir2, int mode)
	{
		try {
			ObjectOutputStream out = new ObjectOutputStream(getUser(HWID).getLoginSocket().getOutputStream());
			out.writeObject(Crypter.encrypt(new DataPackage("RM", HWID, dir, dir2, mode)));
		} catch (Exception e) {}
	}

	public synchronized void sendDataPackage(DataPackage message, String HWID) {
		sendDataPackage(message, getUser(HWID));
	}
	
	public static synchronized void sendDataPackage(DataPackage message, User u) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(u.getLoginSocket().getOutputStream());
			out.writeObject(Crypter.encrypt(message));
		} catch (Exception e) {
			usermanager.addHandle(u, ConnectionAction.LEAVE);
		}
	}

	public synchronized void broadcastMessage(DataPackage message) {
		LinkedList<User> list = getUsers();
		for(User u: list)
		{
			sendDataPackage(message, u);
		}
	}
	
	/**
	 * Der Manager benutzt diese Methode,
	 * um die Socketchannel zu registrieren
	 * */
	public void registerChannel(Channel channel)
	{
		socketreader.registerChannel(channel);
	}
	
	/**
	 * Da gibts wohl nicht viel zu erklären.
	 * Hiermit wird der Server gestoppt!
	 * */
	public void stopServer() {
		serverstate = ServerState.STOPPING;

		if (server != null) {
			try {
				server.close();
			} catch (Exception e) {}
		}
		log("SERVER STOPPED");
		if(!logger.isClosed())
		{
			logger.close();
		}
		if(!usercache.isClosed())
		{
			usercache.close();
		}
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {}
		System.exit(1);
	}
	
	public static void closeSocket(Socket s)
	{
		try {
			s.close();
		} catch (Exception e) {}
	}
	
	protected abstract void registerEvents();

}
