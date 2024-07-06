package de.dion.socket.options;

import de.dion.socket.localobjects.enums.FileWritingOptions;
import de.dion.socket.objects.DataPackage;

public interface Options {

	/** Serverport */
	final int port = 6235;
	
	/** Standart Channel Beschreibung */
	final String defaultChannelDescription = "Standart Socket-Channel!";
	
	/** Mit dieser Variable setzt man fest wie viele Verbindungen 
	 *  mit der selben IP Addresse erlaubt sind. */
	final int maxConnnections = 3;
	
	/** Die HWID des Servers, die den Clients angezeigt wird! */
	final String HWID = "SERVER";
	
	/** Das Packet, dass zu Pingen vom Server an die Clients verwendet wird. */
	final DataPackage pingpacket = new DataPackage("PING", HWID, "OK");
	
	/** Dieser Text wird beim starten des Servers angezeigt. */
	final String startMessage = "\n  ______                       __                    __             \n /      \\                     /  |                  /  |            \n/$$$$$$  |  ______    _______ $$ |   __   ______   _$$ |_           \n$$ \\__$$/  /      \\  /       |$$ |  /  | /      \\ / $$   |   ______ \n$$      \\ /$$$$$$  |/$$$$$$$/ $$ |_/$$/ /$$$$$$  |$$$$$$/   /      |\n $$$$$$  |$$ |  $$ |$$ |      $$   $$<  $$    $$ |  $$ | __ $$$$$$/ \n/  \\__$$ |$$ \\__$$ |$$ \\_____ $$$$$$  \\ $$$$$$$$/   $$ |/  |        \n$$    $$/ $$    $$/ $$       |$$ | $$  |$$       |  $$  $$/         \n $$$$$$/   $$$$$$/   $$$$$$$/ $$/   $$/  $$$$$$$/    $$$$/          \n  ______                                                            \n /      \\                                                           \n/$$$$$$  |  ______    ______   __     __   ______    ______         \n$$ \\__$$/  /      \\  /      \\ /  \\   /  | /      \\  /      \\        \n$$      \\ /$$$$$$  |/$$$$$$  |$$  \\ /$$/ /$$$$$$  |/$$$$$$  |       \n $$$$$$  |$$    $$ |$$ |  $$/  $$  /$$/  $$    $$ |$$ |  $$/        \n/  \\__$$ |$$$$$$$$/ $$ |        $$ $$/   $$$$$$$$/ $$ |             \n$$    $$/ $$       |$$ |         $$$/    $$       |$$ |             \n $$$$$$/   $$$$$$$/ $$/           $/      $$$$$$$/ $$/              \n";
	
	/** Dieser Variable bestimmt das Time out Delay der User in Sekunden. */
	final short timeOutDelay = 11;
	
	/** Dieser String gibt an auf welcher Version sich der Server befindet. */
	final String version = "1.4";
	
	/** FileWritingOptions sagt was gemacht wird, wenn eine
	 *  übertragene Datei bereits existiert.*/
	final FileWritingOptions fileWritingOptions = FileWritingOptions.OVERRIDE;
	
}
