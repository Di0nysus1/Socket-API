package de.dion.socket.localobjects.channel.channels;

import java.net.Socket;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.localobjects.user.User;
import de.dion.socket.objects.DataPackage;

public class MSG extends Channel {

	public MSG() {
		super("MSG");
	}
	
	/**
	 * TODO: umschreiben!
	 * <B>Momentan unused!</B>
	 * (Soll heißen: momentan sendet keiner der aktuellen Clients Sockets auf diesen Channel.)
	 * */
	@Override
	public void onSocketReceive(DataPackage pack, Socket socket) {
		
		log(pack.getHWID() + " -> " + pack.get(0));
		User u = getUser(pack.getHWID());
		pack.add(u.getName());
		System.out.println("\033[0;36m" + pack.getHWID() + " -> \033[0m\033[0;35m" + pack.get(0) + "\033[0m");
		broadcastMessageWithoutBack(pack, u);
	}

}
