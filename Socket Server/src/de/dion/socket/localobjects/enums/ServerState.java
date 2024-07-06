package de.dion.socket.localobjects.enums;

public enum ServerState {

	//ServerState sagt, ob der Server gestartet, gestoppt etc.. wird.
	
	/**
	 * OFFLINE = 0
	 * */
	OFFLINE,
	
	/**
	 * STARTING = 1
	 * */
	STARTING,
	
	/**
	 * RUNNING = 2
	 * */
	RUNNING,
	
	/**
	 * STOPPING = 3
	 * */
	STOPPING;
	
	@Override
	public String toString()
	{
		return this.name().toUpperCase() + " -> " + toInt();
	}
	
	public int toInt()
	{
		switch(this)
		{
			case OFFLINE:
				return 0;
			case STARTING:
				return 1;
			case RUNNING:
				return 2;
			case STOPPING:
				return 3;
			default:
				throw new NullPointerException();
		}
	}
	
}
