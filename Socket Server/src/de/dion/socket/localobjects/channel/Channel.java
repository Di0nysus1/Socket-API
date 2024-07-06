package de.dion.socket.localobjects.channel;

import java.io.ObjectOutputStream;
import java.util.LinkedList;

import de.dion.socket.Server;
import de.dion.socket.SocketReceiveEvent;
import de.dion.socket.UserManager;
import de.dion.socket.localobjects.enums.ConnectionAction;
import de.dion.socket.localobjects.user.User;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.Crypter;

public abstract class Channel extends ChannelAttributes implements SocketReceiveEvent {
	
	public Channel(String name) {
		super(name.toUpperCase());	
	}
	
	protected void log(String text)
	{
		Server.log(text);
	}
	
	protected void logWithSysOut(String text)
	{
		Server.logWithSysOut(text);
	}
	
	protected UserManager getUserManager()
	{
		return Server.usermanager;
	}
	
	protected synchronized void sendDataPackage(DataPackage message, User u) {
		Server.sendDataPackage(message, u);
	}
	
	protected synchronized void sendDataPackage(DataPackage message, String ip) {
		sendDataPackage(message, getUser(ip));
	}
	
	protected synchronized  void broadcastMessageWithoutBack(DataPackage message, User u) {
		LinkedList<User> list = getUsers();
		for(User u1: list)
		{
			if(!u1.getHWID().equals(u.getHWID()))
			{
				sendDataPackage(message, u1);
			}
		}
	}
	
	protected synchronized void broadcastMessage(DataPackage message) {
		LinkedList<User> list = getUsers();
		for(User u: list)
		{
			sendDataPackage(message, u);
		}
	}
	
	protected User getUser(String hwid)
	{
		return getUserManager().getUser(hwid);
	}
	
	protected LinkedList<User> getUsers()
	{
		return getUserManager().getUsers();
	}
	
	protected void debug(String text)
	{
		Server.debug(text);
	}
	
}
