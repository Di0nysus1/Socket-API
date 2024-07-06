package de.dion.socket.localobjects.channel.channels;

import java.net.Socket;

import de.dion.socket.Server;
import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.localobjects.enums.ConnectionAction;
import de.dion.socket.objects.DataPackage;

public class PING extends Channel {

	public PING() {
		super("PING");
	}

	@Override
	/**
	 * Hier kommen die Pings der Clients an.
	 * */
	public void onSocketReceive(DataPackage pack, Socket socket) {
		
		getUserManager().addHandle(pack.getHWID(), ConnectionAction.PING);
		
	}

}
