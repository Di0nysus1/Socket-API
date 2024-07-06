package de.dion.socket.localobjects.channel.channels;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;

public class MSG extends Channel {

	public MSG() {
		super("MSG");
	}
	
	@Override
	public void onSocketReceive(DataPackage pack) {
		if(pack.size() > 1) {
			System.out.println(pack.getHWID() + " (" + pack.get(1) + ") -> " + pack.get(0));
		} else {
			System.out.println(pack.getHWID() + " -> " + pack.get(0));
		}
	}

}
