package de.dion.socket.objects;

import java.io.Serializable;

public class CryptedDataPackage implements Serializable {
	
	/**
	 * Hier drin ist das verschlüsselte DataPackage!
	 */
	private static final long serialVersionUID = -8105911638151887937L;
	private final byte[] bytes;
	
	public CryptedDataPackage(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public byte[] getBytes()
	{
		return bytes;
	}
	
}
