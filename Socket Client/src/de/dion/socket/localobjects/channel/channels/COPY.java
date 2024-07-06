package de.dion.socket.localobjects.channel.channels;

import java.io.File;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.FileHelper;

public class COPY extends Channel {

	public COPY() {
		super("COPY");
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
				sendDataPackage(new DataPackage("COPY", HWID, "Keine guelltige Datei/Verzeichnis -> " + ff.getCanonicalPath()));
			}
			else
			{
				File ft = getFileFromPath(current, to);
				
				try {
					String name = ff.getName();
					if(name.contains("*")) //*
					{
						if(name.contains("."))
						{
							ff = new File(ff.getAbsolutePath().substring(0, ff.getAbsolutePath().length() - (name.length())));
							if(name.startsWith("*"))
							{
								String ending = FileHelper.getFileEnding(name);
								for(File temp: ff.listFiles())
								{
									if(FileHelper.getFileEnding(temp.getName()).equalsIgnoreCase(ending))
									{
										FileHelper.copyFileOrFolder(temp, ft);
									}
								}
							}
							else
							{
								String start = FileHelper.getFileName(name);
								for(File temp: ff.listFiles())
								{
									if(FileHelper.getFileName(temp.getName()).equalsIgnoreCase(start))
									{
										FileHelper.copyFileOrFolder(temp, ft);
									}
								}
							}
						}
						else
						{
							ff = new File(ff.getAbsolutePath().substring(0, ff.getAbsolutePath().length() - (name.length())));
							for(File temp: ff.listFiles())
							{
								FileHelper.copyFileOrFolder(temp, ft);
							}
						}
					}
					else //normal
					{
						FileHelper.copyFileOrFolder(ff, ft);
					}
					sendDataPackage(new DataPackage("COPY", HWID, "Datei(en) wurde(n) erfolgreich kopiert"));
				} catch(Exception e) {
					sendDataPackage(new DataPackage("COPY", HWID, ff.getName() + " konnte nicht kopiert werden!\n" + e.getLocalizedMessage()));
				}
			}
			
		} catch(Exception e) {
			sendDataPackage(new DataPackage("COPY", HWID, "Fehler beim kopieren aufgetraten!\n" + e.getLocalizedMessage()));
		}
		
	}

}
