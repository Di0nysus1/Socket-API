package de.dion.socket.localobjects.user;

import java.net.Socket;

import de.dion.socket.localobjects.enums.ConnectionAction;

public class UserHandle {
	
	User user;
	ConnectionAction action;
	
	/**
	 * Klasse für den UserManager...
	 * */
	public UserHandle(User user, ConnectionAction action)
	{
		this.user = user;
		this.action = action;
	}
	
	public Socket getLoginSocket()
	{
		return user.getLoginSocket();
	}
	
	public String getHWID()
	{
		return user.getHWID();
	}

	public User getUser() {
		return user;
	}

	public ConnectionAction getAction() {
		return action;
	}

}
