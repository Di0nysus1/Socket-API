package de.dion.socket.utils;

/**
 * Helper Klasse für Strings.
 * Enthällt einige Algorithmen zum bearbeiten von Strings
 * 
 * */
public class StringHelper {
	
	/**
	 * sagt ob ein String etwas anderes als die Zeichen im 2. Argument enthällt
	 * <B>Ignorecase!</B>
	 * */
	public static boolean containsOtherCharThanIgnoreCase(String text, String other)
	{
		String s = text.trim().toLowerCase();
		
		for(int i = 0; i < other.length(); i++)
		{
			s = s.replaceAll(other.toLowerCase().charAt(i) + "", "");
		}
		s = s.trim();
		if(s.length() > 0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * sagt ob ein String etwas anderes als die Zeichen im 2. Argument enthällt
	 * <B>Groß und Kleinschreibung wird beachtet!</B>
	 * */
	public static boolean containsOtherCharThan(String text, String other)
	{
		String s = text;
		
		for(int i = 0; i < other.length(); i++)
		{
			s = s.replaceAll(other.charAt(i) + "", "");
		}
		s = s.trim();
		if(s.length() > 0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Sagt ob ein String etwas beinhaltet
	 * <B>Ignorecase!</B>
	 * */
	public static boolean containsIgnoreCase(String text, String inhalt)
	{
		return text.toLowerCase().contains(inhalt.toLowerCase());
	}
	
	/**
	 * Zählt wie oft ein String etwas beinhaltet
	 * <B>Ignorecase!</B>
	 * */
	public static int countContainsIgnoreCase(String text, String inhalt)
	{
		int count = 0;
		
		while(containsIgnoreCase(text, inhalt))
		{
			text = replaceFirstIgnoreCase(text, inhalt, "");
			count++;
		}
		return count;
	}
	
	/**
	 * Zählt wie oft ein String etwas beinhaltet
	 * <B>Groß und Kleinschreibung wird beachtet!</B>
	 * */
	public static int countContains(String text, String inhalt)
	{
		int count = 0;
		
		while(text.contains(inhalt))
		{
			text = replaceFirst(text, inhalt, "");
			count++;
		}
		return count;
	}
	
	/**
	 * Schneidet einen String aus
	 * <B>Beispiel; between("lol|hallo:hi", "|", ":"); -> hallo</B>
	 * <B>Ignorecase!</B>
	 * */
	public static String cutIgnoreCase(String text, String start, String end) throws IllegalStateException
	{
		start = start.toLowerCase();
		end = end.toLowerCase();
		if(!containsIgnoreCase(text, start) || !containsIgnoreCase(text, end))
		{
			throw new IllegalStateException("Text beinhaltet Start oder End nicht!");
		}
		if(start.equals(end) && countContainsIgnoreCase(text, start) < 2)
		{
			throw new IllegalStateException();
		}
		text = text.substring(text.toLowerCase().indexOf(start) + start.length());
		text = text.substring(0, text.toLowerCase().indexOf(end));
		return text;
	}
	
	/**
	 * Schneidet einen String aus
	 * <B>Beispiel; between("lol|hallo:hi", "|", ":"); -> hallo</B>
	 * <B>Groß und Kleinschreibung wird beachtet!</B>
	 * */
	public static String cut(String text, String start, String end) throws IllegalStateException
	{
		if(!text.contains(start) || !text.contains(end))
		{
			throw new IllegalStateException("Text beinhaltet Start oder End nicht!");
		}
		if(start.equals(end) && countContains(text, start) < 2)
		{
			throw new IllegalStateException();
		}
		text = text.substring(text.indexOf(start) + start.length());
		text = text.substring(0, text.indexOf(end));
		return text;
	}
	
	/**
	 * Ich glaube das hier muss man nicht erklären :D
	 * <B>Groß und Kleinschreibung wird beachtet!</B>
	 * */
	public static String replaceFirst(String text, String inhalt, String replacer)
	{
		String txt = text;
		if(text.contains(inhalt))
		{
			txt = text.substring(0, text.indexOf(inhalt));
			txt = txt + replacer + text.substring(text.indexOf(inhalt) + inhalt.length(), text.length());
		}
		return txt;
	}
	
	/**
	 * Ähnlich wie replaceFirst!
	 * <B>Ignorecase!</B>
	 * */
	public static String replaceFirstIgnoreCase(String text, String inhalt, String replacer)
	{
		String txt = text;
		inhalt = inhalt.toLowerCase();
		if(containsIgnoreCase(text, inhalt))
		{
			txt = text.substring(0, text.toLowerCase().indexOf(inhalt));
			txt = txt + replacer + text.substring(text.toLowerCase().indexOf(inhalt) + inhalt.length(), text.length());
		}
		return txt;
	}
	
	private static String decryptHex(String text)
	{
		String[] s = text.toLowerCase().split("g");
		String hex = "";
		
		for(int i = s.length - 1; i >= 0; i--)
		{
			hex += ((char)hexToInt(s[i]));
		}
		
		return hex;
	}
	
	private static String cryptHex(String text)
	{
		
		String s = text;
		String hex = "";
		
		for(int i = s.length() - 1; i >= 0; i--)
		{
			hex += intToHex(s.charAt(i)) + "g";
		}
		
		hex = hex.substring(0, hex.length() - 1);
		
		return hex.toUpperCase();
	}
	
	private static int hexToInt(String hex)
	{
		int count = 0;
		
		
		int hoch = 0;
		for(int i = hex.length() - 1; i >= 0; i--)
		{
			int z = 0;
			switch(hex.charAt(i))
			{
				case 'a':
					z = 10;
					break;
				case 'b':
					z = 11;
					break;
				case 'c':
					z = 12;
					break;
				case 'd':
					z = 13;
					break;
				case 'e':
					z = 14;
					break;
				case 'f':
					z = 15;
					break;
				default:
					z = Integer.parseInt(hex.charAt(i) + "");
			}
			
			count += Math.pow(16, hoch) * z;
			hoch++;
		}
		return count;
	}
	
	private static String intToHex(int n)
	{
		return Integer.toHexString(n);
	}
	
	/**
	 *  Mit dieser Methode wird ein String ins Hexadezimalzahlensystem umgeformt
	 * */
	public static String crypt(String in)
	{
		return in.equals("") ? "" : cryptHex(in);
	}
	
	/**
	 *  Mit dieser Methode wird ein String wieder zurück ins Binärzahlensystem umgeformt
	 *  (Der String wird wieder normal)
	 * */
	public static String decrypt(String in)
	{
		return in.equals("") ? "" : decryptHex(in);
	}
	
	/**
	 *  Diese Methode sagt ob ein String null ist
	 * */
	public static boolean isNull(String text)
	{
		return text == null || text.trim().equals("");
	}	
}
