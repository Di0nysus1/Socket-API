package de.dion.socket.localobjects.enums;

public enum FileWritingOptions {
	
	//FileWritingOptions sagt was gemacht wird, wenn eine
	//übertragene Datei bereits existiert.
	
	/**
	 * Die Datei wird überschrieben
	 * */
	OVERRIDE,
	
	/**
	 * Die Datei wird nicht geschrieben
	 * */
	CANCEL,
	
	/**
	 * Die Datei wird umbenannt
	 * Beispiel: lol.txt -> lol2.txt -> lol3.txt ...
	 * */
	RENAME,
	
	/**
	 * Die Datei an der Stelle wo aufgehört wurde weiterschreiben.
	 * */
	APPEND;
	
}
