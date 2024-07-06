package de.dion.socket.objects;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

import de.dion.socket.utils.IDHelper;

public class DataPackage implements Serializable {
	
	/**
	 * Hier drin wird der Inhalt den man versenden moechte gespeichert
	 */
	private static final long serialVersionUID = 1891392861903128677L;
	private final short randomID;
	private String id;
	private Object[] memory;
	private String hwid;
	
	/**for HWID do
	 * Options.HWID
	 * */
	public DataPackage(String id, String hwid, Object... o){	
		this.id = id;
		this.hwid = hwid;
		this.memory = o;
		this.randomID = (short)ThreadLocalRandom.current().nextInt();
	}
	
	public String getID()
	{
		return id;
	}
	
	public void setID(String id)
	{
		this.id = id;
	}
	
	public String getHWID()
	{
		return hwid;
	}
	
	public void setHWID(String hwid)
	{
		this.hwid = hwid;
	}
	
	public int size()
	{
		return memory.length;
	}
	
	public Object get(int index)
	{
		return memory[index];
	}
	
	public void add(Object value)
	{
		Object[] temp = memory;
		memory = new Object[temp.length + 1];
		for(int i = 0; i < temp.length; i++)
		{
			memory[i] = temp[i];
		}
		memory[temp.length] = value;
	}
	
	public void addFirst(Object value)
	{
		Object[] temp = memory;
		memory = new Object[temp.length + 1];
		memory[0] = value;
		for(int i = 0; i < temp.length; i++)
		{
			memory[i + 1] = temp[i];
		}
	}
	
	public void addLast(Object value)
	{
		this.add(value);
	}
	
	public void remove(int index)
	{
		Object[] temp = memory;
		memory = new Object[temp.length - 1];
		int i2 = 0;
		int i3 = 0;
		for(Object o: temp)
		{
			if(i2 == index)
			{
				i2++;
				continue;
			}
			memory[i3] = temp[i2];
			i2++;
			i3++;
		}
	}
	
	public void removeFirst()
	{
		if(memory.length >= 1)
		{
			remove(0);
		}
	}
	
	public void removeLast()
	{
		if(memory.length >= 1)
		{
			remove(memory.length - 1);
		}
	}
	
	public void clear()
	{
		memory = new Object[0];
	}
	
	public boolean isEmpty()
	{
		return memory.length == 0;
	}
	
	static {
		System.out.println("DataPackage Klasse wurde geladen");
	}
	
}
