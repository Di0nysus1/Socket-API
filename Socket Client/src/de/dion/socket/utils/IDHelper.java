package de.dion.socket.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.concurrent.ThreadLocalRandom;

public class IDHelper {
	
	private static IDHelper id = null;
	private int port = 0;
	private static String id_raw = null;
	private static String id_encoded = null;
	
	private IDHelper() {}
	
	/**
	 * Diese Methode liest die Rohdaten der HardwareID aus.
	 * #RawStyle xD
	 * */
	public static synchronized String getRawID()
	{
		if(id_raw == null)
		{
			id_raw = getNewID();
		}
		return id_raw;
	}
	
	/**
	 * Mit dieser Methode kann man eine HWID MD5 Hashen.
	 * */
	public static synchronized String encodeID(String RAWID)
	{
		String encoded = null;
		try {
			if(id == null)
			{
				id = new IDHelper();
			}
			encoded = id.encryptPassword(RAWID, "-");
		} catch (Exception e) {}
		return encoded;
	}
	
	/**
	 * Diese Methode übergibt die codierte HardwareID
	 * */
	public static synchronized String getEncodedID()
	{
		if(id_encoded == null)
		{
			String local = getRawID();
			id_encoded = id.encryptPassword(local, "-");
		}
		return id_encoded;
	}
	
	private static String getNewID()
	{
		if(id == null)
		{
			id = new IDHelper();
		}
		return id.generateID();
	}
	
	private String generateID()
	{
		String infos = getOsInfos() + "|" + getIpAddress();
		
		//windows
		infos += "|" + getHardWareInfos();
		
		//linux
		infos += "|" + getIF();
		
		if(port == 0)
		{
			port = ThreadLocalRandom.current().nextInt(1, 1000001);
		}
		infos += ";port:" + port;
		
		return infos;
	}
	
	//all
	private String getOsInfos()
	{
		String pcname = "PC";
		try {
			pcname = java.net.InetAddress.getLocalHost().getHostName();
		} catch(Exception e) {}
		return System.getProperty("os.name") + ";" + System.getProperty("os.version") + ";" + System.getProperty("user.name") + ";" + pcname;
	}
	
	//linux
	private String getIF()
	{
		String result = "";
		try {
			
			Process p = Runtime.getRuntime().exec("bash -c ifconfig");
	    	BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
	    	String line;
	    	while ((line = input.readLine()) != null) {
	    		line = line.trim();
	    		line = line.replace(" ", "");
	    		if(!line.equals("") && !line.toLowerCase().contains("name"))
	    		{
	    			result += line;
	    			break;
	    		}
	    	}
	    	input.close();
	    	result +=";";
		} catch (Exception e) {}
		return result;
	}
	
	//all
	private String getIpAddress()
	{
		try {
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			StringBuilder sb = new StringBuilder();
			byte[] mac = network.getHardwareAddress();
			for (int i = 0; i < mac.length; i++)
			{
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			return sb.toString() + ";";
		} catch (Exception e) {}
		return "FF-FF-FF-FF-FF-FF;";
	}
	
	//windows
	private String getHardWareInfos()
	{
		String result = "";
	    try {
	    	Process p = Runtime.getRuntime().exec("cmd /c wmic cpu get name,MaxClockSpeed");
	    	BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
	    	String line;
	    	while ((line = input.readLine()) != null) {
	    		line = line.trim() + ",";
	    		line = line.replace(" ", "");
	    		if(!line.equals(",") && !line.toLowerCase().contains("name"))
	    		{
	    			result += line;
	    		}
	    	}
	    	input.close();
	    	result +=";";
	    } catch(Exception e){}
	    try {
	    	Process p = Runtime.getRuntime().exec("cmd /c wmic bios get serialnumber");
	    	BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
	    	String line;
	    	while ((line = input.readLine()) != null) {
	    		line = line.trim() + ",";
	    		line = line.replace(" ", "");
	    		if(!line.equals(",") && !line.toLowerCase().contains("name"))
	    		{
	    			result += line;
	    		}
	    	}
	    	input.close();
	    	result +=";";
	    } catch(Exception e){}
	    try {
	    	Process p = Runtime.getRuntime().exec("cmd /c wmic DISKDRIVE get SerialNumber");
	    	BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
	    	String line;
	    	while ((line = input.readLine()) != null) {
	    		line = line.trim() + ",";
	    		line = line.replace(" ", "");
	    		if(!line.equals(",") && !line.toLowerCase().contains("name"))
	    		{
	    			result += line;
	    		}
	    	}
	    	input.close();
	    	result +=";";
	    } catch(Exception e){}
	    try {
	    	Process p = Runtime.getRuntime().exec("cmd /c wmic bios get version");
	    	BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
	    	String line;
	    	while ((line = input.readLine()) != null) {
	    		line = line.trim() + ",";
	    		line = line.replace(" ", "");
	    		if(!line.equals(",") && !line.toLowerCase().contains("name"))
	    		{
	    			result += line;
	    		}
	    	}
	    	input.close();
	    	result +=";";
	    } catch(Exception e){}
	    try {
	    	Process p = Runtime.getRuntime().exec("bash -c lscpu");
	    	BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
	    	String line;
	    	while ((line = input.readLine()) != null) {
	    		line = line.trim() + ",";
	    		line = line.replace(" ", "");
	    		if(!line.equals(",") && !line.toLowerCase().contains("name"))
	    		{
	    			result += line;
	    		}
	    	}
	    	input.close();
	    	result +=";";
	    } catch(Exception e){}
	    
	    return(result.trim().replaceAll("\n", "").replaceAll("	", ""));
	}
	
	private String encryptPassword(String text, String seperator)
	{
		String port = text.substring(text.indexOf(";port:"));
		text = text.substring(0, text.indexOf(";port:"));
		
	    String sha1 = "";
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("MD5");
	        crypt.reset();
	        crypt.update(text.getBytes("UTF-8"));
	        sha1 = byteToHex(crypt.digest(), seperator);
	    }
	    catch(NoSuchAlgorithmException| UnsupportedEncodingException e) {}
	    return sha1 + port.substring(5);
	}

	private String byteToHex(final byte[] hash, String seperator)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    
	    String out = "";
	    for(int i = 0; i < result.length(); i++)
	    {
	    	if(i == 8 || i == 12 || i == 16 || i == 20)
	    	{
	    		out += seperator;
	    	}
	    	out += result.charAt(i);
	    }
	    
	    return out.toUpperCase();
	}

}
