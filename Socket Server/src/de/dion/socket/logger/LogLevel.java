package de.dion.socket.logger;

public enum LogLevel {

	INFORMATION, WARNING, ERROR, DEBUG;
	
	@Override
	public String toString()
	{
		return this.name().toUpperCase();
	}
	
}
