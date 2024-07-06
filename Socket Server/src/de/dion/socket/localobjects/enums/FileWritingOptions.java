package de.dion.socket.localobjects.enums;

public enum FileWritingOptions {
	
	//FileWritingOptions sagt was gemacht wird, wenn eine
	//�bertragene Datei bereits existiert.
	
	/**
	 * Die Datei wird �berschrieben
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
	 * Die Datei an der Stelle wo aufgeh�rt wurde weiterschreiben.
	 * */
	APPEND;
	
}
