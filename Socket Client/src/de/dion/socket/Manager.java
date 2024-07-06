package de.dion.socket;

import de.dion.socket.localobjects.channel.channels.MSG;
import de.dion.socket.localobjects.channel.channels.PING;

public class Manager extends Client {
	
	/**
	 * Nimmt Adresse und Port aus den {@link de.dion.socket.options.Options}
	 * */
	public Manager() {
		super();
	}
	
	/**
	 * Nimmt gegebene Adresse und Port
	 * */
	public Manager(String serverAdress, int serverPort) {
		super(serverAdress, serverPort);
	}
	
	/**
	 * Hier werden die Socket-Channels deklariert!
	 * NUR die Channel die hier angegeben sind funktionieren auch!
	 * Kommt ein Paket an, dessen Channel hier nicht angegeben ist, wird es auch nicht executed
	 * */
	@Override
	protected void registerEvents() {
		
//		registerChannel(new CD());
//		registerChannel(new CMD());
//		registerChannel(new COPY());
//		registerChannel(new DIR());
//		registerChannel(new MC());
//		registerChannel(new NANO_READ());
		registerChannel(new PING());
//		registerChannel(new MOVE());
//		registerChannel(new RN());
		registerChannel(new MSG());
//		registerChannel(new GETFILE());
//		registerChannel(new DOWNLOAD());
		
	}

}
