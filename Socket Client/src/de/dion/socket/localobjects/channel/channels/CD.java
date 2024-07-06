package de.dion.socket.localobjects.channel.channels;

import java.io.File;
import java.io.IOException;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;

public class CD extends Channel {

	public CD() {
		super("CD");
	}

	@Override
	public void onSocketReceive(DataPackage pack) {
		
		try {
			if(pack.size() > 1 && pack.get(1) != null)
			{
				String current = (String) pack.get(0);
				String path = (String) pack.get(1);
				File f = getFileFromPath(current, path);
				
				if(f.exists())
				{
					if(f.isDirectory())
					{
						sendDirectory(f.getCanonicalPath(), false);
					}
					else
					{
						sendDirectory("Kein guelltiges Verzeichnis -> " + f.getCanonicalPath(), true);
					}
				}
				else
				{
					sendDirectory("Kein guelltiges Verzeichnis -> " + f.getCanonicalPath(), true);
				}
			}
			else
			{
				sendDirectory(new File("./").getCanonicalPath(), false);
			}
		} catch(IOException e) {}
	}
	
	private void sendDirectory(String dir, boolean error)
	{
		DataPackage dp = new DataPackage("CD", HWID, dir, error);
		sendDataPackage(dp);
	}

}
