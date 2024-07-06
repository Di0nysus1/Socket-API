package de.dion.socket.localobjects.channel;

import de.dion.socket.options.Options;

public class ChannelAttributes implements Options {
	
	private final String name;
	private String description;
	
	protected ChannelAttributes(String name)
	{
		this.name = name;;
		this.description = defaultChannelDescription;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	protected void setDescription(String description)
	{
		this.description = description;
	}
	
	@Override
	public String toString()
	{
		return getName() + ": " + getDescription();
	}

}
