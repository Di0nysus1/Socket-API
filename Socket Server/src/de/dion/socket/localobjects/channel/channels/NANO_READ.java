package de.dion.socket.localobjects.channel.channels;

import java.net.Socket;
import java.util.LinkedList;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.main.Main;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.StringHelper;

public class NANO_READ extends Channel {

	public NANO_READ() {
		super("NANO_READ");
	}

	@Override
	public void onSocketReceive(DataPackage pack, Socket socket) throws Exception {
		
		if((boolean)pack.get(0) == false) //false bedeutet in diesem Fall ob ein Fehler aufgetreten ist.
		{
			//hat funktioniert
			LinkedList<String> lines = (LinkedList<String>) pack.get(1);
			Main.nano_dir = (String) pack.get(2);
			Main.nano = true;
			Main.nano_line = (int)pack.get(3);
			
			for(int i = 0; i < 25; i++)
			{
				System.out.println("");
			}
			
			log(pack.getHWID() + " [NANO_READ] -> " + Main.nano_dir);
			System.out.println("\033[0;36m" + pack.getHWID() + ":\033[0m\033[0;35m " + Main.nano_dir + "\033[0m");
			for(int i = 0; i < lines.size(); i++)
			{
				log("[NANO_READ] [" + (Main.nano_line + i) + "] ->  " + StringHelper.decrypt(lines.get(i)));
				System.out.println("\033[0;36m[\033[0m\033[0;35m" + (Main.nano_line + i) + "\033[0m\033[0;36m]\033[0m " + StringHelper.decrypt(lines.get(i)));
			}
		}
		else
		{
			//keine gülltige datei oder sonstige Fehler
			log(pack.getHWID() + " [NANO] -> " + pack.get(1));
			System.out.println("\033[0;36m" + pack.getHWID() + ":\033[0m\033[0;35m" + pack.get(1) + "\033[0m");
			Main.nano = false;
		}
		
	}

}
