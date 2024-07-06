package de.dion.socket.utils;

import java.util.concurrent.ThreadLocalRandom;

public class MathHelper {
	
	private static long count = 0;
	
	public static long nextCount()
	{
		if(count == 9223372036854775700L)
		{
			count = 0;
		}
		return count++;
	}
	
	public static int generateRandom(int min, int max)
	{
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	public static int toInt(double d)
	{
		return (int)d;
	}
	
	public static int toInt(float f)
	{
		return (int)f;
	}
	
}
