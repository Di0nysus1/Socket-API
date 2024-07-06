package de.dion.socket.utils;

public class TimeHelper {
	
private long lastMS;
	
	public TimeHelper()
	{
		this.lastMS = 0L;
	}
	
	/**
	 * Sagt ob ein bestimmtes Delay abgelaufen ist.
	 * @param delay in Millisekunden (1000L = 1Sek)
	 * */
	public boolean isDelayComplete(long delay)
	{
		if(System.currentTimeMillis() - lastMS >= delay)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Returnt das jetzige Delay.
	 * */
	public Long getDelay()
	{
		return System.currentTimeMillis() - lastMS;
	}
	
	/**
	 * Resetet das Delay.
	 * */
	public void reset()
	{
		this.lastMS = System.currentTimeMillis();
	}
	
}
