package de.dion.socket.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Date;

public class Logger {
	
	private final File logfile;
	private BufferedWriter bw;
	private boolean allowInfo = true;
	private boolean allowError = true;
	private boolean allowWarn = true;
	private boolean allowDebug = true;
	private boolean closely = true;
	private boolean closed = false;
	
	/**
	 * Hier kann der Logger erstellt werden.
	 * Das LogFile, dass man angibt muss existieren!
	 * */
	public Logger(File logfile) throws FileNotFoundException
	{
		if(logfile.exists())
		{
			this.logfile = logfile;
		}
		else
		{
			throw new FileNotFoundException("Logfile \"" + logfile.getName() + "\" konnte nicht gefunden werden!");
		}
		this.setClosely(false);
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile, true), Charset.forName("UTF-8")));
	}
	
	public boolean isAllowAll()
	{
		return isAllowInfo() && isAllowError() && isAllowWarn() && isAllowDebug();
	}
	
	public void allowAll()
	{
		setAllowError(true);
		setAllowInfo(true);
		setAllowWarn(true);
		setAllowDebug(true);
	}
	
	public boolean isAllowInfo() {
		return allowInfo;
	}

	public void setAllowInfo(boolean allowInfo) {
		this.allowInfo = allowInfo;
	}

	public boolean isAllowError() {
		return allowError;
	}

	public void setAllowError(boolean allowError) {
		this.allowError = allowError;
	}

	public boolean isAllowWarn() {
		return allowWarn;
	}

	public void setAllowWarn(boolean allowWarning) {
		this.allowWarn = allowWarning;
	}
	
	public boolean isAllowDebug() {
		return allowDebug;
	}

	public void setAllowDebug(boolean allowDebug) {
		this.allowDebug = allowDebug;
	}
	
	/**
	 * Closely bedeutet, dass der Logger alles Kompakt in die LogDatei schreibt
	 * ohne Leerzeilen, welche das ganze übersichtlicher machen.
	 * */
	public boolean isClosely() {
		return closely;
	}
	
	/**
	 * Closely bedeutet, dass der Logger alles Kompakt in die LogDatei schreibt
	 * ohne Leerzeilen, welche das ganze übersichtlicher machen.
	 * */
	public void setClosely(boolean closely) {
		this.closely = closely;
	}
	
	public boolean isAllowed(LogLevel ll)
	{
		switch(ll)
		{
			case ERROR:
				return this.isAllowError();
			case INFORMATION:
				return this.isAllowInfo();
			case WARNING:
				return this.isAllowWarn();
			case DEBUG:
				return this.isAllowDebug();
			default:
				return false;
		}
	}
	
	/**
	 * Mit dieser Methode werden Nachrichten geloggt.
	 * */
	public synchronized void log(String msg, LogLevel loglevel)
	{
		if(isAllowed(loglevel) && !isClosed())
		{
			try {
				bw.write(new Date().toLocaleString() + "");
				bw.newLine();
				bw.write(loglevel + ": " + msg);
				bw.newLine();
				if(!this.isClosely())
				{
					bw.newLine();
				}
				bw.flush();
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Mit dieser Methode werden Informationen geloggt.
	 * */
	public void info(String msg)
	{
		this.log(msg, LogLevel.INFORMATION);
	}
	
	/**
	 * Mit dieser Methode werden Fehler geloggt.
	 * */
	public void error(String msg)
	{
		this.log(msg, LogLevel.ERROR);
	}
	
	/**
	 * Mit dieser Methode werden Warnungen geloggt.
	 * */
	public void warn(String msg)
	{
		this.log(msg, LogLevel.WARNING);
	}
	
	/**
	 * Mit dieser Methode werden Debug-Messages geloggt.
	 * */
	public void debug(String msg)
	{
		this.log(msg, LogLevel.DEBUG);
	}
	
	public boolean isClosed()
	{
		return closed;
	}
	
	public void close()
	{
		closed = true;
		try {
			bw.close();
		} catch (IOException e) {}
	}
	
	public File getLogFile()
	{
		return logfile;
	}

}
