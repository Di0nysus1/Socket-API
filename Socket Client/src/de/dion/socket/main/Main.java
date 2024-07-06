package de.dion.socket.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import de.dion.socket.Manager;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.IDHelper;
import de.dion.socket.utils.StringHelper;

public class Main {

	private static Manager m = null;
	
	//für  Java 8
	public static void main(String[] args) {
		String msg = null;
		BufferedReader bw = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
		System.out.println("Bitte gib einen Namen ein.");
		while(true)
		{
			try {
				msg = bw.readLine();
			} catch (IOException e) {}
			if(msg != null && !msg.trim().equals(""))
			{
				if(m == null) {
					final String temp = msg.trim();
					System.out.println("Starte als " + temp);
					
					m = new Manager();
					m.setName(temp);
					m.start();
					
				} else {
					execute(msg.trim());
				}
			}
			msg = null;
		}
	}
	
	private static void execute(String msg)
	{
		m.sendDataPackage(new DataPackage("MSG", IDHelper.getEncodedID(), msg));
	}

}
