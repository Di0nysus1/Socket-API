package de.dion.socket.localobjects.channel;

import java.io.File;

import de.dion.socket.Client;
import de.dion.socket.PackageSender;
import de.dion.socket.SocketReceiveEvent;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.PathUtils;

public abstract class Channel extends ChannelAttributes implements SocketReceiveEvent, PathUtils {
	
	private PackageSender sender;
	
	public Channel(String name) {
		super(name.toUpperCase());
	}
	
	public void setSender(PackageSender sender) {
		this.sender = sender;
	}
	
	/**
	 *  Bitte diese Methode zum senden von DataPackages benutzen!
	 * */
	protected boolean sendDataPackage(DataPackage pack)
	{
		return sender.sendDataPackage(pack);
	}
	
}
