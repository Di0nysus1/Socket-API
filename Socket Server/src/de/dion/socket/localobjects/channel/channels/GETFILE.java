package de.dion.socket.localobjects.channel.channels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.localobjects.file.DataBundle;
import de.dion.socket.localobjects.file.FileData;
import de.dion.socket.localobjects.user.User;
import de.dion.socket.objects.DataPackage;

public class GETFILE extends Channel {

	public GETFILE() {
		super("GETFILE");
	}
	
	@Override
	public void onSocketReceive(DataPackage pack, Socket socket) throws Exception {
		
		final User u = getUser(pack.getHWID());
		System.out.println((int)System.currentTimeMillis());
		final FileData fd;
		
		if(pack.size() == 1)
		{
			System.out.println(pack.get(0).toString());
			return;
		}
		
		fd = u.getFileData(pack.get(0).toString());
		if(pack.size() == 2)
		{
			final int end = (int)pack.get(1);
			System.out.println("b ist angekommen " + end);
			if(end == -1)
			{
				u.endFileData(fd);
				System.out.println("finish (leere Datei)");
				return;
			}
			fd.setEndindex(end);
		}
		else
		{
			final int index = (int)pack.get(1);
			byte[] data = (byte[])pack.get(2);
			
			synchronized (fd.getBundles()) {
				fd.addData(new DataBundle(index, data));
			}
			System.out.println("index: " + index);
			if((index + 1) % 10 == 0 && fd.getNewindex() == fd.getCounter())
			{
				System.out.println("bims 10. packet");
				fd.setNewindex(index);
			}
		}
		
		
		if(fd.getNewindex() > fd.getCounter())
		{
			synchronized (fd.getBundles())
			{
				if(fd.getNewindex() > fd.getCounter())
				{
					boolean vollständig = true;
					for(int i = fd.getCounter() + 1; i <= fd.getNewindex(); i++)
					{
						if(!fd.contains(i))
						{
							vollständig = false;
							break;
						}
					}
					if(vollständig)
					{
						for(int i = fd.getCounter() + 1; i <= fd.getNewindex(); i++)
						{
							fd.write(i);
						}
						fd.setCounter(fd.getNewindex());
					}
					else
					{
						return;
					}
				}
			}
		}
		
		if(fd.getEndindex() != -1 && fd.getEndindex() > fd.getCounter())
		{
			synchronized (fd.getBundles())
			{
				if(fd.getEndindex() != -1 && fd.getEndindex() > fd.getCounter())
				{
					boolean vollständig = true;
					for(int i = fd.getEndindex(); i >= fd.getCounter() + 1; i--)
					{
						if(!fd.contains(i))
						{
							vollständig = false;
							break;
						}
					}
					System.out.println("vollständig " + vollständig);
					if(vollständig) //alle datenpakete angekommen!!! (rest muss jetzt geschrieben werden
					{
						for(int i = fd.getCounter() + 1; i <= fd.getEndindex(); i++)
						{
							System.out.println("writing " + i);
							fd.write(i);
						}
						fd.setCounter(fd.getEndindex());	
						if(!fd.getBundles().isEmpty())
						{
							System.out.println("bundle ist nicht leer!!!! ");
						}
						System.out.println("finish");
						u.endFileData(fd);
					}
				}
			}
		}
	}
	
}
