package de.dion.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.LinkedList;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.CryptedDataPackage;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.Crypter;

public class EventHandler {
	
	private LinkedList<Channel> channels;
	
	public EventHandler()
	{
		channels = new LinkedList<>();
	}
	
	/**
	 * Hier werden die SocketChannel registriert!
	 * */
	public void registerChannel(Channel c)
	{
		channels.add(c);
	}
	
	/**
	 * Die Listening Methode im Client ruft diese Methode auf,
	 * um die Sockets, die der Server an den Client sendet auszulesen.
	 * */
	public void read(Object raw)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				work(raw);
			}
		}).start();
	}
	
	/**
	 * Diese Methode liest die angekommenen Sockets aus
	 * und ruft das SocketReceiveEvent im jeweiligen Channel auf.
	 * (siehe socket.localobjects.channel.channels.PING, Zeile 14)
	 * */
	private void work(Object raw)
	{
		try {
			if (raw != null && raw instanceof CryptedDataPackage)
			{
				final DataPackage msg = (DataPackage) Crypter.decrypt((CryptedDataPackage)raw);
				if(msg == null)
				{
					return;
				}
				for(Channel channel: channels)
				{
					if(msg.getID().equalsIgnoreCase(channel.getName()))
					{
						try {
							channel.onSocketReceive(msg);
						} catch(Exception e) {}
						break;
					}
				}
			}
		} catch(Exception e) {}
	}
	
}
