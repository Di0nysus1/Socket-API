package de.dion.socket.localobjects.channel.channels;

import java.io.File;
import java.net.Socket;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;

public class GETDIRECTORY extends Channel {

	public GETDIRECTORY() {
		super("GETDIRECTORY");
	}

	@Override
	public void onSocketReceive(DataPackage pack, Socket socket) throws Exception {
		
		final String path = "files" + File.separator + "cache" + File.separator + pack.get(0).toString().replaceAll(":", "");
		final File f = new File(path);
		f.mkdirs();
	}

}
