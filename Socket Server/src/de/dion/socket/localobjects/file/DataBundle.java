package de.dion.socket.localobjects.file;

import de.dion.socket.localobjects.user.User;

public class DataBundle {
	
	private final byte[] data;
	private final int index;
	
	public DataBundle(int index, byte[] data)
	{
		this.index = index;
		this.data = data;
	}
	
	
	public byte[] getData()
	{
		return this.data;
	}
	
	public int getIndex()
	{
		return this.index;
	}
	
}
