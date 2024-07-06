package de.dion.socket.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileHelper {

	/**
	 * Kopiere eine Datei oder einen Ordner
	 * */
	public static void copyFileOrFolder(File from, File to) throws IOException
	{
		from = new File(from.getCanonicalPath());
		to = new File(to.getCanonicalPath());
		
		if(from.isDirectory())
		{
			to = new File(to.getCanonicalPath() + File.separator + from.getName());
		}
		else if(from.isFile() && !to.isFile())
		{
			to = new File(to.getCanonicalPath() + File.separator + from.getName());
		}
		
		if(to.getCanonicalPath().equalsIgnoreCase(from.getCanonicalPath()))
		{
			return;
		}
		
	    if (from.isDirectory())
	    {
	    	copyFolder(from, to, StandardCopyOption.REPLACE_EXISTING);
	    }
	    else
	    {
	        createParentFolder(to);
	        copyFile(from, to, StandardCopyOption.REPLACE_EXISTING);
	    }
	}

	private static void copyFolder(File from, File to, CopyOption... options) throws IOException
	{
	    if (!to.exists())
	    {
	    	to.mkdirs();
	    }
	    File[] files = from.listFiles();
	    if (files != null)
	    {
	        for (File f: files)
	        {
	            File newFile = new File(to.getAbsolutePath() + File.separator + f.getName());
	            if (f.isDirectory())
	            {
	                copyFolder(f, newFile, options);
	            }
	            else
	            {
	                copyFile(f, newFile, options);
	            }
	        }
	    }
	}

	private static void copyFile(File from, File to, CopyOption... options) throws IOException
	{
	    Files.copy(from.toPath(), to.toPath(), options);
	}

	private static void createParentFolder(File file)
	{
	    File parent = file.getParentFile();
	    if (parent != null && !parent.exists())
	    {
	    	parent.mkdirs();
	    }
	}
	
	/**
	 * Übergibt den Namen einer Datei
	 * Beispiel:
	 * <B>hallo.txt -> hallo</B>
	 * */
	public static String getFileName(String name)
	{
		if(name.contains("."))
		{
			for(int i = name.length() - 1; i >= 0; i--)
			{
				if(name.charAt(i) == '.')
				{
					name = name.substring(0, i);
					break;
				}
			}
		}
		return name;
	}
	
	/**
	 * Übergibt das Format einer Datei
	 * Beispiel:
	 * <B>hallo.txt -> txt</B>
	 * */
	public static String getFileEnding(String name)
	{
		if(name.contains("."))
		{
			for(int i = name.length() - 1; i >= 0; i--)
			{
				if(name.charAt(i) == '.')
				{
					name = name.substring(i + 1, name.length());
					break;
				}
			}
		}
		return name;
	}
	
	/**
	 * Mit dieser Methode kannst du Dateien und Ordner verschieben
	 * */
	public static void move(File ff, File ft) throws IOException, IllegalStateException
	{
		String name = ff.getName();
		
		if(name.contains("*"))
		{
			if(name.contains("."))
			{
				ff = new File(ff.getAbsolutePath().substring(0, ff.getAbsolutePath().length() - (name.length())));
				if(name.startsWith("*"))
				{
					String ending = getFileEnding(name);
					for(File temp: ff.listFiles())
					{
						if(getFileEnding(temp.getName()).equalsIgnoreCase(ending))
						{
							moveFile(temp, ft);
						}
					}
				}
				else
				{
					String start = getFileName(name);
					for(File temp: ff.listFiles())
					{
						if(getFileName(temp.getName()).equalsIgnoreCase(start))
						{
							moveFile(temp, ft);
						}
					}
				}
			}
			else
			{
				ff = new File(ff.getAbsolutePath().substring(0, ff.getAbsolutePath().length() - (name.length())));
				for(File temp: ff.listFiles())
				{
					moveFile(temp, ft);
				}
			}
		}
		else
		{
			moveFile(ff, ft);
		}
	}
	
	private static void moveFile(File from, File to) throws IOException, IllegalStateException
	{
		from = new File(from.getCanonicalPath());
		to = new File(to.getCanonicalPath());
		if(from.exists() && to.exists())
		{
			if(to.isFile())
			{
				throw new IllegalStateException();
			}
			moveFileOrFolder(from, to);
		}
		else
		{
			if(!from.exists())
			{
				throw new FileNotFoundException(from.getAbsolutePath() + " konnte nicht gefunden werden!");
			}
			if(!to.exists())
			{
				throw new FileNotFoundException(to.getAbsolutePath() + " konnte nicht gefunden werden!");
			}
		}
	}
	
	private static void moveFileOrFolder(File from, File to, CopyOption c) throws IOException
	{
		Files.move(from.toPath(), to.toPath(), c);
	}
	
	private static void moveFileOrFolder(File from, File to) throws IOException
	{
		to = new File(to.getPath() + File.separator + from.getName());
		moveFileOrFolder(from, to, StandardCopyOption.REPLACE_EXISTING);
	}
	
	/**
	 * Mit dieser Methode kannst du Dateien und Ordner umbenennen.
	 * */
	public static void renameFileOrFolder(File file, String name) throws IOException, IllegalStateException
	{
		if(file.exists())
		{
			if(name.contains("/"))
			{
				throw new IllegalStateException();
			}
			File n = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length()) + File.separator + name);
			Files.move(file.toPath(), n.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		else
		{
			throw new FileNotFoundException();
		}
	}
	
	
}
