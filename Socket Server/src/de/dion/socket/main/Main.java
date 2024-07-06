package de.dion.socket.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import de.dion.socket.Manager;
import de.dion.socket.Server;
import de.dion.socket.localobjects.user.User;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.options.Options;
import de.dion.socket.utils.StringHelper;

public class Main {

	private static volatile Manager m = null;
	public static String dir; 
	public static String nano_dir = "";
	public static int nano_line = 1;
	private static int state = 1;
	public static String User = null;
	private static ArrayList<String> lines = new ArrayList<>();
	public static boolean nano = false;
	public static boolean debugmode;
	
	public static void main(String[] args)
	{
		System.out.println("Server wird gestartet!");
		
		debugmode = false;
		if(args.length > 0)
		{
			if(args[0].toLowerCase().contains("debug"))
			{
				debugmode = true;
			}
		}
//		debugmode = true;
		
		dir = "";
		lines.clear();
		nano = false;
		nano_dir = "";
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				m = new Manager();
			}
		}).start();
		
		while(state == 1)
		{
			String msg = null;
			BufferedReader bw = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
			try {
				msg = bw.readLine();
			} catch (IOException e) {}
			if(msg != null && !msg.equals(""))
			{
				msg = msg.trim();
				if(nano && !m.isOnline(User))
				{
					nano = false;
				}
				if(nano)
				{
					String[] ka = msg.split(" ");
					String[] args2 = new String[ka.length - 1];
					String cmd = ka[0];
					
					
					
					if(ka.length > 1)
					{
						for(int i = 1; i < ka.length; i++)
						{
							args2[i - 1] = ka[i];
						}
					}
					String g = null;
					for(String v: args2)
					{
						if(g == null)
						{
							g = v;
							continue;
						}
						g+= (" " + v);
					}
					nano_execute(cmd, g);
				}
				else
				{
					String[] ka = msg.split(" ");
					String[] args2 = new String[ka.length - 1];
					String cmd = ka[0];
					if(ka.length > 1)
					{
						for(int i = 1; i < ka.length; i++)
						{
							args2[i - 1] = ka[i].trim();
						}
					}
					String g = null;
					for(String v: args2)
					{
						if(g == null)
						{
							g = v;
							continue;
						}
						g+= (" " + v);
					}
					Server.logger.info("<Screen>: " + cmd + (g == null ? "" : " " + g.trim()));
					execute(cmd, args2, g == null ? g : g.trim());
				}
			}
		}
	}
	
	private static void execute(String cmd, String[] args, String arg)
	{
		if(cmd.equalsIgnoreCase("help"))
		{
			System.out.println("---Command Hilfe---");
			System.out.println("help | zeigt dir alle Befehle an");
			System.out.println("list | listet alle verbundenen Client auf");
			System.out.println("cls | bereinigt den Chat");
			System.out.println("nano | Dateien auslesen und scannen");
			System.out.println("cd | navigierst du in ein Verzeichnis");
			System.out.println("rm");
			System.out.println("setUser | Spieler wählen");
			System.out.println("ls | Verzeichnis auflisten ");
			System.out.println("mc | Minecraft Konsole");
			System.out.println("cmd | Terminal benutzen");
			System.out.println("bc | eine Nachricht an alle User senden");
			System.out.println("kick | wirft den User vom Socket-Server");
			System.out.println("stop | stoppt den Socket-Server");
			System.out.println("copy | Dateien kopieren");
			System.out.println("move | Dateien verschieben");
			System.out.println("rn | Dateien umbenennen");
			System.out.println("info | zeige Userinformationen an");
		}
		else if(cmd.equalsIgnoreCase("download"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					if(args.length > 1)
					{
						String filename = args[0];
						String url = "";
						for(int i = 1; i < args.length; i++)
						{
							url += args[i] + " ";
						}
						url = url.trim();
						m.sendDataPackage(new DataPackage("DOWNLOAD", Options.HWID, dir, filename, url), User);
					}
					else
					{
						System.out.println(cmd + " <Dateiname> <URL>");
					}
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("getfile"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					if(args.length > 0)
					{
						m.sendDataPackage(new DataPackage("GETFILE", Options.HWID, dir, arg), User);
					}
					else
					{
						System.out.println(cmd + " <Datei>");
					}
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("info") || cmd.equalsIgnoreCase("userinfo"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					User u = m.getUser(User);
					System.out.println("\033[0;36m" + User + " -> \033[0m\033[0;93m" + u.getRAWID() + " IP: " + u.getLoginSocket().getInetAddress() + "\033[0m");
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("rename") || cmd.equalsIgnoreCase("rn"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					if(args.length > 1)
					{
						String msg = arg;
						String from = "";
						String to = "";
						if(StringHelper.countContains(msg, "\"") > 1)
						{
							from = StringHelper.cut(msg, "\"", "\"");
							msg = StringHelper.replaceFirst(msg, "\"" + from + "\"", "");
						}
						else
						{
							from = args[0];
							msg = StringHelper.replaceFirst(msg, from, "");
						}
						if(StringHelper.countContains(msg, "\"") > 1)
						{
							to = StringHelper.cut(msg, "\"", "\"");
						}
						else
						{
							to = msg.trim();
						}
						
						System.out.println("Benenne " + from + " um zu " + to);
						m.sendDataPackage(new DataPackage("RN", Options.HWID, dir, from, to), User);
					}
					else
					{
						System.out.println(cmd + " <file> <file>");
					}
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("copy") || cmd.equalsIgnoreCase("cp"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					if(args.length > 1)
					{
						String msg = arg;
						String from = "";
						String to = "";
						if(StringHelper.countContains(msg, "\"") > 1)
						{
							from = StringHelper.cut(msg, "\"", "\"");
							msg = StringHelper.replaceFirst(msg, "\"" + from + "\"", "");
						}
						else
						{
							from = args[0];
							msg = StringHelper.replaceFirst(msg, from, "");
						}
						if(StringHelper.countContains(msg, "\"") > 1)
						{
							to = StringHelper.cut(msg, "\"", "\"");
						}
						else
						{
							to = msg.trim();
						}
						
						System.out.println("Kopiere " + from + " nach " + to);
						m.sendDataPackage(new DataPackage("COPY", Options.HWID, dir, from, to), User);
					}
					else
					{
						System.out.println("copy <file> <file>");
					}
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("move") || cmd.equalsIgnoreCase("mv"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					if(args.length > 1)
					{
						String msg = arg;
						String from = "";
						String to = "";
						if(StringHelper.countContains(msg, "\"") > 1)
						{
							from = StringHelper.cut(msg, "\"", "\"");
							msg = StringHelper.replaceFirst(msg, "\"" + from + "\"", "");
						}
						else
						{
							from = args[0];
							msg = StringHelper.replaceFirst(msg, from, "");
						}
						if(StringHelper.countContains(msg, "\"") > 1)
						{
							to = StringHelper.cut(msg, "\"", "\"");
						}
						else
						{
							to = msg.trim();
						}
						
						System.out.println("Verschiebe " + from + " nach " + to);
						m.sendDataPackage(new DataPackage("MOVE", Options.HWID, dir, from, to), User);
					}
					else
					{
						System.out.println("move <file> <file>");
					}
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("cc") || cmd.equalsIgnoreCase("cls") || cmd.equalsIgnoreCase("clear"))
		{
			for(int i = 0; i < 30; i++)
			{
				System.out.println("");
			}
			if(User != null && m.isOnline(User))
			{
				m.getDirectory(User, dir, "");
			}
		}
		else if(cmd.equalsIgnoreCase("nano"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					if(args.length > 0)
					{
						nano_line = 1;
						nano_dir = "";
						m.openNano(User, dir, arg);
					}
					else
					{
						System.out.println("Nano <Datei>");
					}
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("rm"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					if(args.length > 0)
					{
						if(args[0].equalsIgnoreCase("-r"))
						{
							if(args.length > 1)
							{
								String file = null;
								for(int i = 1; i < args.length; i++)
								{
									file = file == null ? args[i] : file + " " + args[i];
								}
								m.delFile(User, dir, file, 1);
							}
							else
							{
								System.out.println("rm [<-r>] <Datei>");
							}
						}
						else
						{
							m.delFile(User, dir, arg, 0);
						}
					}
					else
					{
						System.out.println("rm [<-r>] <Datei>");
					}
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("setUser"))
		{
			if(args.length > 0)
			{
				if(m.isOnline(args[0]))
				{
					User = args[0];
					dir = "";
					System.out.println("User wurde auf " + args[0] + " gesetzt.");
				}
				else
				{
					System.out.println(args[0] + " ist gerade nicht online!");
				}
			}
			else
			{
				if(m.getUsers().size() == 1)
				{
					User = m.getUsers().get(0).getHWID();
					dir = "";
					System.out.println("User wurde auf " + User + " gesetzt.");
				}
				else
				{
					System.out.println("setUser <HWID>");
				}
			}
		}
		else if(cmd.equalsIgnoreCase("cd"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					m.getDirectory(User, dir, arg);
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("ls") || cmd.equalsIgnoreCase("dir"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					m.getLS(User, dir);
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("mc"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					if(args.length > 0)
					{
						m.sendDataPackage(new DataPackage("MC", Options.HWID, arg), User);
					}
					else
					{
						System.out.println("mc <Command>");
					}
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("cmd"))
		{
			if(User != null)
			{
				if(m.isOnline(User))
				{
					if(args.length > 0)
					{
						m.sendDataPackage(new DataPackage("CMD", Options.HWID, arg), User);
					}
					else
					{
						System.out.println("cmd <Command>");
					}
				}
				else
				{
					System.out.println(User + " ist gerade nicht online!");
				}
			}
			else
			{
				System.out.println("Kein User gefunden!");
			}
		}
		else if(cmd.equalsIgnoreCase("bc") || cmd.equalsIgnoreCase("broadcast"))
		{
			if(args.length > 0)
			{
				if(m.getUsers().size() > 0)
				{
					m.broadcastMessage(new DataPackage("MSG", Options.HWID, arg));
					Server.log("Server -> BroadCast Console Command: " + arg);
					System.out.println("Server -> " + arg);
				}
				else
				{
					System.out.println("Es ist niemand online!");
				}
			}
			else
			{
				System.out.println(cmd + " <Nachricht>");
			}
		}
		else if(cmd.equalsIgnoreCase("list"))
		{
			System.out.println("Online User:");
			for(User u: m.getUsers())
			{
				System.out.println("\033[0;36m" + u.getHWID() + "\033[0m");
			}
			System.out.println("isgesammt " + m.getUsers().size() + " User online.");
		}
		else if(cmd.equalsIgnoreCase("kick"))
		{
			if(args.length > 0)
			{
				if(m.isOnline(args[0]))
				{
					m.disconnectUser(args[0]);
					System.out.println(args[0] + " wurde gekickt!");
				}
				else
				{
					System.out.println(args[0] + " ist nicht online!");
				}
			}
			else
			{
				System.out.println("kick <HWID>");
			}
		}
		else if(cmd.equalsIgnoreCase("stop"))
		{
			System.out.println("Server wird heruntergefahren...");
			state = 0;
			m.stopServer();
			Thread.currentThread().stop();
		} 
		
		else if(cmd.equalsIgnoreCase("amg") || cmd.equalsIgnoreCase("mercedes") || cmd.equalsIgnoreCase("benz"))
		{
			
			for(int i = 0; i < 30; i++)
			{
				System.out.println("");
			}
				System.out.println("  ___  ___  ___ _____          _                 _          _      ");
				System.out.println(" / _ \\ |  \\/  ||  __ \\        | |               | |        | |       ");    
				System.out.println("/ /_\\ \\| .  . || |  \\/    ___ | |_   __ _  _ __ | |_   ___ | |_        ");  
				System.out.println("|  _  || |\\/| || | __    / __|| __| / _` || '__|| __| / _ \\| __|         ");
				System.out.println("| | | || |  | || |_\\ \\   \\__ \\| |_ | (_| || |   | |_ |  __/| |_  _  _  _ ");
				System.out.println("\\_| |_/\\_|  |_/ \\____/   |___/ \\__| \\__,_||_|    \\__| \\___| \\__|(_)(_)(_)");
				
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				
				for(int i = 0; i < 30; i++)
				{
					System.out.println("");
				}
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
				

		System.out.println("______ ______  _   _ ___  ___   ______ ______  _   _ ___  ___      ");
		System.out.println("| ___ \\| ___ \\| | | ||  \\/  |   | ___ \\| ___ \\| | | ||  \\/  |      ");
		System.out.println("| |_/ /| |_/ /| | | || .  . |   | |_/ /| |_/ /| | | || .  . |      ");;
		System.out.println("| ___ \\|    / | | | || |\\/| |   | ___ \\|    / | | | || |\\/| |      ");
		System.out.println("| |_/ /| |\\ \\ | |_| || |  | |   | |_/ /| |\\ \\ | |_| || |  | | _  _ ");
		System.out.println("\\____/ \\_| \\_| \\___/ \\_|  |_/   \\____/ \\_| \\_| \\___/ \\_|  |_/(_)(_)");
		
		

		
		}		
		
		
		
		else
		{
			System.out.println(cmd + " wurde nicht gefunden!");
		}
	}
	
	private static void nano_execute(String cmd, String arg)
	{
		if(cmd.equals("[B"))
		{
			//5 nach oben
			nano_line += 5;
		}
		else if(cmd.equals("[A"))
		{
			//5 nach unten
			nano_line = ((nano_line - 5) >= 1) ? nano_line - 5 : 1;
		}
		else if(cmd.equals("[C"))
		{
			//1 nach oben
			nano_line += 1;
		}
		else if(cmd.equals("[D"))
		{
			//1 nach unten
			nano_line = ((nano_line - 1) >= 1) ? nano_line - 1 : 1;
		}
		else if(cmd.equalsIgnoreCase("end"))
		{
			nano_line = (Integer.MAX_VALUE - 30);
		}
		else if(cmd.equalsIgnoreCase("line"))
		{
			if(arg == null)
			{
				System.out.println("line <Zeile>");
			}
			else
			{
				try {
					nano_line = Integer.parseInt(arg);
				} catch(Exception e) {
					System.out.println("line <Zeile>");
				}
			}
		}
		else if(cmd.equalsIgnoreCase("start"))
		{
			nano_line = 1;
		}
		else
		{
			if(cmd.equalsIgnoreCase("exit") || cmd.equalsIgnoreCase("quit"))
			{
				nano = false;
				if(User != null && m.isOnline(User))
				{
					m.getDirectory(User, dir, "");
				}
			}
			else if(cmd.equalsIgnoreCase("scan"))
			{
				if(User != null)
				{
					if(m.isOnline(User))
					{
						m.openNano(User, nano_dir, "0xt77_49penis333");
					}
					else
					{
						System.out.println(User + " ist gerade nicht online!");
					}
				}
				else
				{
					System.out.println("Kein User gefunden!");
				}
			}
			else if(cmd.equalsIgnoreCase("help"))
			{
				System.out.println("help, exit, scan, start, end, line <zeile>");
			}
			return;
		}
		if(User != null)
		{
			if(m.isOnline(User))
			{
				m.openNano(User, nano_dir, "0xt77_49penis");
			}
			else
			{
				System.out.println(User + " ist gerade nicht online!");
				nano = false;
			}
		}
		else
		{
			System.out.println("Kein User gefunden!");
			nano = false;
		}
	}
	
}
