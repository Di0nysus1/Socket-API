package de.dion.socket.localobjects.channel.channels;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;

public class MC extends Channel {

	public MC() {
		super("MC");
	}

	@Override
	public void onSocketReceive(DataPackage pack) {
		
//		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), pack.get(0).toString());
	}

}
