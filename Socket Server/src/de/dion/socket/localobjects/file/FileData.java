package de.dion.socket.localobjects.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import de.dion.socket.Server;
import de.dion.socket.localobjects.enums.FileWritingOptions;
import de.dion.socket.main.Main;
import de.dion.socket.options.Options;
import de.dion.socket.utils.FileHelper;

public final class FileData implements Options {
	
	private final LinkedList<DataBundle> bundles;
	private final String path;
	
	private FileOutputStream fos;
	private int counter = -1;
	private int endindex = -1;
	private int newindex = -1;
	
	public FileData(String path)
	{
		this.path = path;
		this.bundles = new LinkedList<>();
	}
	
	
	public final void startWriter() throws SecurityException, IOException
	{
		final String path = "files" + File.separator + "cache" + File.separator + this.path.replaceAll(":", "");
		File f = new File(path);
		if(f.exists())
		{
			if(fileWritingOptions == FileWritingOptions.CANCEL)
			{
				Server.debug("Die Datei \"" + path + "\" existiert bereits!");
				return;
			}
			else if(fileWritingOptions == FileWritingOptions.RENAME)
			{
				final String ending = FileHelper.getFileEnding(f.getName());
				final String name = FileHelper.getFileName(path);
				int i = 2;
				while(f.exists())
				{
					f = new File(name + " (" + i + ")." + ending);
					System.out.println("versuche " + f.getAbsolutePath());
					i++;
				}
			}
		}
		final String dirpath = path.substring(0, path.length() - new File(path).getName().length());
		final File dir = new File(dirpath);
		dir.mkdirs();
		f.createNewFile();
		
		fos = new FileOutputStream(f, fileWritingOptions == FileWritingOptions.APPEND);
	}
	
	public final void closeWriter()
	{
		try {
			fos.close();
		} catch (IOException e) {}
	}

	public final void addData(final DataBundle db)
	{
		bundles.add(db);
	}
	
	public final void write(final int index) throws IOException
	{
		final DataBundle db = getData(index);
		write(db.getData());
		bundles.remove(db);
	}
	
	private final void write(final byte[] data) throws IOException
	{
		try {
			fos.write(data);
			fos.flush();
		} catch(IOException e) {
			if(Main.debugmode)
			{
				e.printStackTrace();
			}
		}
	}
	
	public final void remove(final DataBundle db)
	{
		bundles.remove(db);
	}
	
	public final boolean contains(final int index)
	{
		for(final DataBundle db: bundles)
		{
			if(db.getIndex() == index)
			{
				return true;
			}
		}
		return false;
	}
	
	public final DataBundle getData(final int index)
	{
		for(final DataBundle db: bundles)
		{
			if(db.getIndex() == index)
			{
				return db;
			}
		}
		return null;
	}
	
	public final LinkedList<DataBundle> getBundles()
	{
		return this.bundles;
	}
	
	public final String getPath()
	{
		return this.path;
	}
	
	public int getCounter()
	{
		return counter;
	}
	
	public void setCounter(int counter)
	{
		this.counter = counter;
	}
	
	public int getEndindex()
	{
		return endindex;
	}
	
	public void setEndindex(int endindex)
	{
		this.endindex = endindex;
	}
	
	public int getNewindex()
	{
		return newindex;
	}
	
	public void setNewindex(int newindex)
	{
		this.newindex = newindex;
	}
	
}
