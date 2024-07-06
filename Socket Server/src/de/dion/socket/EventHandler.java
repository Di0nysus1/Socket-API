package de.dion.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.localobjects.enums.ConnectionAction;
import de.dion.socket.main.Main;
import de.dion.socket.objects.CryptedDataPackage;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.Crypter;
import de.dion.socket.utils.IDHelper;

public class EventHandler {
	
	private LinkedList<Channel> channels;
	
	public EventHandler()
	{
		channels = new LinkedList<>();
	}
	
	public void registerChannel(Channel c)
	{
		channels.add(c);
	}
	
	/**
	 * Die Listening Methode im Server ruft diese Methode auf,
	 * um die Sockets, die die Clients an den Server senden auszulesen.
	 * */
	public void read(Socket socket)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				work(socket);
			}
		}).start();
	}
	
	/**
	 * Diese Methode liest die angekommenen Sockets aus
	 * und ruft das SocketReceiveEvent im jeweiligen Channel auf.
	 * (siehe socket.localobjects.channel.channels.PING, Zeile 20)
	 * */
	private void work(Socket s)
	{
		Object raw = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			raw = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {if(Main.debugmode) {e.printStackTrace();}}
		if (raw != null && raw instanceof CryptedDataPackage)
		{
			final DataPackage msg = Crypter.decrypt((CryptedDataPackage)raw);
			if(msg != null)
			{
				for (Channel socketchannel: channels)
				{
					if (msg.getID().equalsIgnoreCase(socketchannel.getName()))
					{
						if(msg.getID().equalsIgnoreCase("LOGIN"))
						{
							try {
								socketchannel.onSocketReceive(msg, s);
								break;
							} catch (Exception e) {
								if(Main.debugmode)
								{
									e.printStackTrace();
								}
							}
						}
						else
						{
							String hwid = msg.getHWID();
							if(hwid == null)
							{
								Server.logger.warn(hwid + " Fehler beim codieren der HWID! (HWID ist NULL)");
								if(Main.debugmode)
								{
									System.out.println(hwid + " Fehler beim codieren der HWID! (HWID ist NULL)");
								}
								Server.closeSocket(s);
								return;
							}
							String crypted = null;
							
							try {
								crypted = IDHelper.encodeID(hwid.split(":")[0] + s.getInetAddress() + ";port:" + hwid.split(":")[1]);
							} catch (Exception e) {
								
								Server.logger.warn(hwid + " Fehler beim codieren der HWID!");
								if(Main.debugmode)
								{
									System.out.println(hwid + " Fehler beim codieren der HWID!");
									e.printStackTrace();
								}
								Server.closeSocket(s);
								return;
							}
							
							if(crypted != null)
							{
								msg.setHWID(crypted);
								if(Server.usermanager.isOnline(msg.getHWID()))
								{
									try {
										socketchannel.onSocketReceive(msg, s);
									} catch (Exception e) {
										if(Main.debugmode)
										{
											e.printStackTrace();
										}
									}
								}
							}
							else
							{
								Server.logger.warn(hwid + " Fehler beim codieren der HWID!");
							}
							Server.closeSocket(s);
							break;
						}
						
						break;
					}
				}
			}
		}
	}
	

}
