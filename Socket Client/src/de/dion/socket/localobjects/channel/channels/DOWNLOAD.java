package de.dion.socket.localobjects.channel.channels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;

public class DOWNLOAD extends Channel {

	public DOWNLOAD() {
		super("DOWNLOAD");
	}

	@Override
	public void onSocketReceive(DataPackage pack) {
		
		try {
			URL url = new URL(pack.get(2).toString());
			InputStream is = url.openStream();
			String current = pack.get(0).toString();
			String file = pack.get(1).toString();
			FileOutputStream fw = new FileOutputStream(getFileFromPath(current, file));

			byte[] buf = new byte[1048576];
			int y = 0;
			while((y = is.read(buf)) != -1)
			{
				fw.write(Arrays.copyOf(buf, y));
			}
			is.close();
			fw.close();
			sendDataPackage(new DataPackage("DOWNLOAD", HWID, "Die Datei \"" + file + "\" wurde erfolgreich heruntergeladen"));
			
		} catch (IOException e) {
			sendDataPackage(new DataPackage("DOWNLOAD", HWID, "Es ist ein Fehler beim herunterladen der Datei aufgetreten!"));
		}
	}
	
}
