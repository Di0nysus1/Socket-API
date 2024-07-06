package de.dion.socket;

import de.dion.socket.localobjects.channel.channels.LOGIN;
import de.dion.socket.localobjects.channel.channels.MSG;
import de.dion.socket.localobjects.channel.channels.PING;

public class Manager extends Server {

	/**
	 *  Rufe diesen Constructor auf um den Socket-Server zu starten!
	 * */
	public Manager() {
		super();
	}
	
	/**
	 * Hier werden die Socket-Channels deklariert!
	 * */
	@Override
	protected void registerEvents() {
		
//		registerChannel(new CD());
//		registerChannel(new CMD());
//		registerChannel(new COPY());
//		registerChannel(new DIR());
		registerChannel(new LOGIN());
		registerChannel(new MSG());
//		registerChannel(new NANO_READ());
		registerChannel(new PING());
//		registerChannel(new MOVE());
//		registerChannel(new RN());
//		registerChannel(new GETFILE());
//		registerChannel(new GETDIRECTORY());
//		registerChannel(new DOWNLOAD());
		
	}
	

}
