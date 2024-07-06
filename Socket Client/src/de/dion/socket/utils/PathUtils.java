package de.dion.socket.utils;

import java.io.File;

public interface PathUtils {
	
	/** 
	 *  <B>current</B> = Der Pfad an dem man sich (mit cd) befindet
	 *  <P>
	 *  <B>path</B> = Die Datei die man von <B>current</B> aus getten möchte
	 * */
	default File getFileFromPath(String current, String path)
	{
		File file = null;
		if(path.startsWith("/"))
		{
			file = new File(path);
		}
		else
		{
			if(current.equals(""))
			{
				file = new File("./" + path);
			}
			else
			{
				file = new File(current + "/" + path);
			}
		}
		return file;
	}
	
}
