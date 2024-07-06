package de.dion.socket.options;

import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.Crypter;

public interface Options {
	
	/** Serveradresse */
	String serverAdress = "localhost";
	
	/** Serverport */
	int serverPort = 6235;
	
	/** Soll der Client wenn er die Verbindung verliert automatisch versuch zu reconnecten? */
	boolean autoReconnect = true;
	
	/** Dies ist ist die Maximale Dateigröße (in Bytes) die eine Datei zum auslesen haben darf!
	 *  <B>104857600L = 100MiB</B> */
	Long maxFileSize = 10485760L;
	
	/** Standart Channel Beschreibung */
	String defaultChannelDescription = "Standart Socket-Channel!";
	
	/** Die codierte HWID des Clients, die dem Server angezeigt wird! */
	final String HWID = Crypter.getHWID();
	
	/** Das Packet, dass zu Pingen vom Client an den Server verwendet wird. */
	DataPackage pingPacket = new DataPackage("PING", HWID, "OK");
	
	/** Keep Alive Delay in Sekunden 
	 * Wenn x Sekunden kein ping vom Server ankam, Reconnect. */
	short pingDelay = 11;
	
	/** Dieser String gibt an auf welcher Version sich der Client befindet. */
	final String version = "1.4";
	
}
