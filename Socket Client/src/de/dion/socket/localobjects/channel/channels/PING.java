package de.dion.socket.localobjects.channel.channels;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;

public class PING extends Channel {

	public PING() {
		super("PING");
	}
	
	//diese Methode wird aufgerufen wenn ein Ping Packet vom Server an den Client ankommt.
	@Override
	public void onSocketReceive(DataPackage pack) {
		
	}

}
