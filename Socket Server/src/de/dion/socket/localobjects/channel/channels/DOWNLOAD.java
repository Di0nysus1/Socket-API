package de.dion.socket.localobjects.channel.channels;

import java.net.Socket;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;

public class DOWNLOAD extends Channel {

	public DOWNLOAD() {
		super("DOWNLOAD");
	}

	@Override
	public void onSocketReceive(DataPackage pack, Socket socket) throws Exception {
		
		log(pack.getHWID() + " [DOWNLOAD] -> " + pack.get(0));
		System.out.println("\033[0;36m" + pack.getHWID() + ":\033[0m\033[0;35m " + pack.get(0) + "\033[0m");
	}

}
