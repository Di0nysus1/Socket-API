package de.dion.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.localobjects.enums.ClientState;
import de.dion.socket.objects.CryptedDataPackage;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.options.Options;
import de.dion.socket.utils.Crypter;
import de.dion.socket.utils.IDHelper;
import de.dion.socket.utils.TimeHelper;

/**
  ______                       __                    __            
 /      \                     /  |                  /  |           
/$$$$$$  |  ______    _______ $$ |   __   ______   _$$ |_          
$$ \__$$/  /      \  /       |$$ |  /  | /      \ / $$   |  ______ 
$$      \ /$$$$$$  |/$$$$$$$/ $$ |_/$$/ /$$$$$$  |$$$$$$/  /      |
 $$$$$$  |$$ |  $$ |$$ |      $$   $$<  $$    $$ |  $$ | __$$$$$$/ 
/  \__$$ |$$ \__$$ |$$ \_____ $$$$$$  \ $$$$$$$$/   $$ |/  |       
$$    $$/ $$    $$/ $$       |$$ | $$  |$$       |  $$  $$/        
 $$$$$$/   $$$$$$/   $$$$$$$/ $$/   $$/  $$$$$$$/    $$$$/         
  ______   __  __                        __                        
 /      \ /  |/  |                      /  |                       
/$$$$$$  |$$ |$$/   ______   _______   _$$ |_                      
$$ |  $$/ $$ |/  | /      \ /       \ / $$   |                     
$$ |      $$ |$$ |/$$$$$$  |$$$$$$$  |$$$$$$/                      
$$ |   __ $$ |$$ |$$    $$ |$$ |  $$ |  $$ | __                    
$$ \__/  |$$ |$$ |$$$$$$$$/ $$ |  $$ |  $$ |/  |                   
$$    $$/ $$ |$$ |$$       |$$ |  $$ |  $$  $$/                    
 $$$$$$/  $$/ $$/  $$$$$$$/ $$/   $$/    $$$$/                      

*/

public abstract class Client implements Options {
	
	private volatile Socket loginSocket;
	private PackageSender sender;
	private String name = "none"; //Test stuff für Chat programm
	private TimeHelper delay = null;
	private EventHandler eventHandler;
	private ClientState state = ClientState.OFFLINE;
	
	
	protected Client(String serverAdress, int serverPort) {
		InetSocketAddress address = new InetSocketAddress(serverAdress, serverPort);
		sender = new PackageSender(address);
	}
	
	protected Client() {
		InetSocketAddress address = new InetSocketAddress(serverAdress, serverPort);
		sender = new PackageSender(address);
	}
	
	/**
	 * Hiermit wird der SocketClient gestartet.
	 * */
	public void start()
	{
		//starten
		init();
		
		//autoreconnect
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				autoReconnect();
			}
		}).start();
	}
	
	/**
	 * Hier wird der Client bei Verbindungsverlust neugestartet.
	 * */
	private void autoReconnect()
	{
		while(autoReconnect && state != ClientState.STOPPING) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}
			if(state == ClientState.RECONNECTING) {
				//connection lost
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
				init();
			}
		}
	}
	
	/**
	 * Starte den Client...
	 * */
	private void init() {
		if(state != ClientState.STOPPING) {
			state = ClientState.STARTING;
			loginSocket = null;
			eventHandler = new EventHandler();
			registerEvents();
			
			delay = new TimeHelper();
			delay.reset();
			login();
			startPingThread();
			startListening();
		}
	}
	
	/** 
	 * Diese Methode Pingt vom Client aus den Server an und
	 * schaut ob der Verbindungsversuch erfolgreich war.
	 * */
	private void startPingThread()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (state == ClientState.RUNNING || state == ClientState.STARTING) {
					try {
						Thread.sleep(2000L);
						
						//schau ob man den server anpingen kann, wenn nicht dann connection close
						if(!sendDataPackage(pingPacket))
						{
							if(loginSocket != null)
							{
								loginSocket.close();
								loginSocket = null;
							}
							closeConnection();
							break;
						}
						
						//wenn schon X Sekunden kein keep alivevom server kam dann conncection close
						if(delay.isDelayComplete(pingDelay * 1000L))
						{
							if(loginSocket != null)
							{
								loginSocket.close();
								loginSocket = null;
							}
							closeConnection();
							break;
						}
					} catch (Exception e) {}
				}
				
			}
		}, "Pingthread").start();
	}
	
	/**
	 * Wenn die Verbindung abbricht oder austimed, sorgt diese Methode dafür,
	 * dass die Verbindung komplett geschlossen wird.
	 * */
	private void closeConnection() {
//		System.out.println("connection closed");
		if(state != ClientState.STOPPING) {
			state = autoReconnect ? ClientState.RECONNECTING : ClientState.STOPPING;
		}
	}
	
	/**
	 * Diese Methode verbindet sich mit dem
	 * Server und speichert das Loginsocket ab.
	 * */
	public boolean login()
	{
		try {
			try {
				Thread.sleep(500);
				loginSocket.close();
			} catch(Exception e) {
			}
			loginSocket = null;
			loginSocket = new Socket();
			loginSocket.connect(sender.getAddress(), 2000);
			
			ObjectOutputStream os = new ObjectOutputStream(loginSocket.getOutputStream());
			os.writeObject(Crypter.encrypt(new DataPackage("LOGIN", Crypter.getHWID(), IDHelper.getRawID(), this.name)));
		} catch (IOException | NullPointerException ex) {
			System.out.println("Login gescheitert, Konnte nicht zum Server verbinden");
			return false;
		}
		return true;
	}
	
	/**
	 * Wenn der Server ein Packet(Socket) an diesen Client
	 * sendet, kommt es in dieser Methode an, und wird dann
	 * im EventHandler ausgelesen.
	 * */
	private void startListening()
	{
		if (state == ClientState.STARTING)
		{
			state = ClientState.RUNNING;
			new Thread(new Runnable() {
				
				@Override
				public void run()
				{
					while (state == ClientState.RUNNING)
					{
						try {
							if(loginSocket == null || loginSocket.isClosed() || !loginSocket.isConnected()) {
								System.out.println("schließe connection weil:");
								if(loginSocket == null)
								{
									System.out.println("loginsocket == null");
								} else {
									if(loginSocket.isClosed())
									{
										System.out.println("loginsocket is closed");
									}
									if(!loginSocket.isConnected())
									{
										System.out.println("loginsocket is not connected");
									}
								}
								closeConnection();
								break;
							}
							ObjectInputStream ois = new ObjectInputStream(loginSocket.getInputStream());
							Object raw = ois.readObject();
							delay.reset();
//							System.out.println("packet kam an");
							eventHandler.read(raw);
						} catch(Exception e) {}
					}
				}
			}).start();
		}
	}
	
	/**
	 * Mit dieser Methode kannst du ein DataPackage an
	 * den Server senden.
	 * Das DataPackage wird von ganz allein verschlüsselt und
	 * dann übertragen.
	 * */
	public boolean sendDataPackage(DataPackage pack)
	{
		return sender.sendDataPackage(pack);
	}
	
	/**
	 * Der Manager benutzt diese Methode,
	 * um die Socketchannel zu registrieren
	 * */
	protected void registerChannel(Channel channel)
	{
		channel.setSender(sender);
		eventHandler.registerChannel(channel);
	}
	
	/**
	 * Da gibts wohl nicht viel zu erklären.
	 * Hiermit wird die Verbindung abgebrochen und der
	 * Client gestoppt!
	 * */
	public void stopClient()
	{
		state = ClientState.STOPPING;
		try {
			loginSocket.close();
		} catch(Exception e) {}
		loginSocket = null;
	}
	
	public ClientState getState() {
		return this.state;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected abstract void registerEvents();
}
