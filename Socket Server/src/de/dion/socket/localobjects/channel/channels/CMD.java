package de.dion.socket.localobjects.channel.channels;

import java.net.Socket;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;

public class CMD extends Channel {

	public CMD() {
		super("CMD");
	}

	@Override
	public void onSocketReceive(DataPackage pack, Socket socket) {
		
		log(pack.getHWID() + " -> \n" + pack.get(0));
		System.out.println("\033[0;36m" + pack.getHWID() + " -> \033[0m\n\033[0;35m" + pack.get(0) + "\033[0m");
		
	}

}
