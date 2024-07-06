package de.dion.socket;

import java.net.Socket;

import de.dion.socket.objects.DataPackage;

public interface SocketReceiveEvent {
	
	/**
	 * Dieses Event wird aufgerufen, wenn der Working-Thread
	 * ein guelltiges Socket ausliest
	 * */
	public abstract void onSocketReceive(DataPackage pack);
	
}
