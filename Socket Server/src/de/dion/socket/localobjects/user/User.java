package de.dion.socket.localobjects.user;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

import de.dion.socket.localobjects.enums.UserGroup;
import de.dion.socket.localobjects.file.FileData;
import de.dion.socket.options.Options;
import de.dion.socket.utils.TimeHelper;

public class User implements Options {
	
	private String HWID;
	private final String RAWID;
	private final String ipAddress;
	private final Socket loginsocket;
	private final String name;
	private TimeHelper delay = null;
	private UserGroup userType;
	private final LinkedList<FileData> files;
	
	/**
	 * Diese Klasse beinhaltet die Daten der User, die sich
	 * mit dem Socket Server verbunden haben.
	 * */
	public User(Socket loginsocket, String HWID, String RAWID, String name)
	{
		this.loginsocket = loginsocket;
		this.HWID = HWID;
		this.RAWID = RAWID;
		this.delay = new TimeHelper();
		this.refreshPing();
		this.userType = UserGroup.USER;
		this.files = new LinkedList<>();
		this.name = name;
		
		InetAddress ip = this.loginsocket.getInetAddress();
		if(ip == null)
		{
			this.ipAddress = "null";
		}
		else
		{
			this.ipAddress = ip.toString();
		}
	}
	
	public void endFileData(FileData fd)
	{
		synchronized (files) {
			fd.closeWriter();
			files.remove(fd);
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public FileData getFileData(String path)
	{
		synchronized (files) {
			for(FileData fd: files)
			{
				if(fd.getPath().equalsIgnoreCase(path))
				{
					return fd;
				}
			}
			final FileData fd = new FileData(path);
			try {
				fd.startWriter();
			} catch (SecurityException | IOException e) {}
			files.add(fd);
			return fd;
		}
	}
	
	public String getIPAddress()
	{
		return this.ipAddress;
	}
	
	public UserGroup getUserGroup()
	{
		return userType;
	}
	
	public void setUserGroup(UserGroup userType)
	{
		this.userType = userType;
	}
	
	public void refreshPing()
	{
		this.delay.reset();
	}
	
	public boolean isTimedOut()
	{
		return this.delay.isDelayComplete(timeOutDelay * 1000);
	}
	
	public Long getDelay() {
		return this.delay.getDelay();
	}

	public String getHWID()
	{
		return HWID;
	}

	public void setHWID(String HWID) {
		this.HWID = HWID;
	}

	public Socket getLoginSocket()
	{
		return loginsocket;
	}
	
	public String getRAWID()
	{
		return RAWID;
	}

}
