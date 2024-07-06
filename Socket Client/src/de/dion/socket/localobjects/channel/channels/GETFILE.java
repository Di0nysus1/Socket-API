package de.dion.socket.localobjects.channel.channels;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;

public class GETFILE extends Channel {

	public GETFILE() {
		super("GETFILE");
	}
	
	private Thread a = null;
	
	@Override
	public void onSocketReceive(DataPackage pack) {
		
		String dir1 = (String) pack.get(0);
		String dir2 = (String) pack.get(1);
		File f = getFileFromPath(dir1, dir2);
		
		if(f.exists())
		{
			if(f.isFile())
			{
				readFile(f);
			}
			else
			{
				checkDirectory(f);
			}
		}
		else
		{
			try {
				sendDataPackage(new DataPackage("GETFILE", HWID, "Die Datei \"" + f.getCanonicalPath() + "\" konnte nicht gefunden werden!"));
			} catch (IOException e) {}
		}
	}
	
	private final void readFile(final File f)
	{
		try {
			String path = f.getCanonicalPath();
			FileInputStream fis = new FileInputStream(f);
			
			byte[] data = new byte[1048576];
			int y = 0,
				index = -1;
			while((y = fis.read(data)) != -1)
			{
				index++;
				send(index, Arrays.copyOf(data, y), path);
				data = new byte[1048576];
			}
			fis.close();
			
			if(a != null)
			{
				try {
					a.join();
				} catch (InterruptedException e) {}
			}
			sendDataPackage(new DataPackage("GETFILE", HWID, path, index));
			
		} catch(IOException e) {e.printStackTrace();}
	}
	
	private final void checkDirectory(File f)
	{
		File[] l = f.listFiles();
		if(l.length == 0)
		{
			sendDirectory(f);
			return;
		}
		for(File f2: l)
		{
			if(f2 == null)
			{
				continue;
			}
			if(f2.isDirectory())
			{
				checkDirectory(f2);
			}
			else
			{
				readFile(f2);
			}
		}
	}
	
	private final void send(final int index, final byte[] data, final String path)
	{
		Thread b = new Thread(new Runnable() {
			
			@Override
			public void run() {
				sendNow(index, data, path);
			}
		});
		if(a != null)
		{
			try {
				a.join();
			} catch (InterruptedException e) {}
		}
		a = b;
		a.start();
	}
	
	private final void sendNow(final int index, final byte[] data, final String path)
	{
		sendDataPackage(new DataPackage("GETFILE", HWID, path, index, data));
	}
	
	private final void sendDirectory(final File f)
	{
		try {
			sendDataPackage(new DataPackage("GETDIRECTORY", HWID, f.getCanonicalPath()));
		} catch (IOException e) {}
	}
	
}
