package de.dion.socket.localobjects.channel.channels;

import java.io.IOException;
import java.net.Socket;

import de.dion.socket.Server;
import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.localobjects.enums.ConnectionAction;
import de.dion.socket.localobjects.user.User;
import de.dion.socket.main.Main;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.IDHelper;

public class LOGIN extends Channel {

	public LOGIN() {
		super("LOGIN");
	}
	
	@Override
	/**
	 * Diese Methode wird aufgerufen, wenn sich ein Client mit dem Server verbindet!
	 * */
	public void onSocketReceive(DataPackage pack, Socket socket) {
		String hwid = pack.getHWID();
		if(hwid != null && !hwid.trim().equals("") && hwid.contains(":") && pack.size() > 0 && pack.get(0) instanceof String)
		{
			User u = new User(socket, hwid, pack.get(0).toString(), pack.get(1).toString());
			if(getUserManager().getConnections(socket.getInetAddress()) < maxConnnections)
			{
				if(checkHWID(hwid, u.getRAWID()))
				{
					if(Main.debugmode)
					{
						System.out.println(socket.getInetAddress());
					}
					
					String crypted = IDHelper.encodeID(hwid.split(":")[0] + socket.getInetAddress() + ";port:" + hwid.split(":")[1]);
					
					if(crypted != null)
					{
						u.setHWID(crypted);
						getUserManager().addHandle(u, ConnectionAction.JOIN);
					}
					else
					{
						Server.usercache.warn(hwid + " " + u.getRAWID() + " Fehler beim codieren der HWID!");
						close(socket);
					}
				}
				else
				{
					Server.usercache.warn(hwid + " " + u.getRAWID() + " ID stimmt NICHT überein!");
					close(socket);
				}
			}
			else
			{
				Server.usercache.warn(hwid + " " + u.getRAWID() + " Zu viele offene Verbindungen!");
				close(socket);
			}
		}
		else
		{
			Server.usercache.warn(hwid + " HWID ist null oder etwas der gleichen!");
			close(socket);
		}
	}
	
	private boolean checkHWID(String HWID, String RAWID)
	{
		return IDHelper.encodeID(RAWID).equals(HWID);
	}
	
	private void close(Socket socket)
	{
		Server.closeSocket(socket);
	}

}
