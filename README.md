# Die gute alte Socket API
made with Eclipse

Die API ist dafür gemacht um eine stabile Verbindung mit auto reconnect zwischen einem Server und vielen Clients aufzubauen. Ein Anwendungs-Beispiel wäre ein IRC Chat innerhalb eines Minecraft Clients zwischen anderen Benutzern des Minecraft Clients.

Es gibt in `src/de/dion/socket/localobjects/channel/channels` ein paar Channel-Klassen für zb. das Übertragen von Files, ausführen von Konsolen Befehlen usw...
Diese Channels sind im `src/\de/dion/socket/Manager.java` standart mäßig deaktiviert / auskommentiert. Wenn du diese Funktionen nutzen möchtest musst du sie dort aktivieren. Wenn nicht, darfst du die Channels (Bis auf PING und LOGIN) gerne löschen.

Was ihr damit macht ist euere Sache.

### Code Beispiel für den Socket Client:
```java
Manager m = new Manager();
m.setName("Hacker_1337");
m.start();

m.sendDataPackage(new DataPackage("MSG", IDHelper.getEncodedID(), "Hello World!"));
```
Die Main Klasse im SocketClient braucht ihr nicht unbedingt, sie ist nur ein Beispiel für ein Chat Programm.

## Version 1.4

![Server-Client connection](pic-1.png)
![Ping und Login erklaert](pic-2.png)
