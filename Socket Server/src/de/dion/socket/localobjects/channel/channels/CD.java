package de.dion.socket.localobjects.channel.channels;

import java.net.Socket;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.main.Main;
import de.dion.socket.objects.DataPackage;

public class CD extends Channel {

	public CD() {
		super("CD");
		this.setDescription("Ein Channel zum navigieren im Dateisystem der User");
	}

	@Override
	public void onSocketReceive(DataPackage pack, Socket socket) {
		
		if(pack.size() > 1)
		{
			log(pack.getHWID() + " [CD] -> " + pack.get(0));
			System.out.println("\033[0;36m" + pack.getHWID() + " -> \033[0m\033[0;35m" + pack.get(0) + "\033[0m");
			if(!((boolean)pack.get(1)))
			{
				Main.dir = pack.get(0).toString();
			}
		}
		else
		{
			log("ERROR: CD \n" + pack.getHWID());
		}
		
	}

}
