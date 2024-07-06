package de.dion.socket.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import de.dion.socket.objects.CryptedDataPackage;
import de.dion.socket.objects.DataPackage;
import de.dion.socket.options.CryptKey;

public class Crypter implements CryptKey {
	
	private static int port = 0;
	private static SecretKey key;
	
	/**
	 * Benutze diese Methode um ein DataPackage vor dem versenden zu verschlüsseln
	 * Die Methode <B>Client.sendDataPackage(DataPackage pack)</B> tut dies Automatisch!
	 * */
	public static CryptedDataPackage encrypt(DataPackage datapackage)
	{
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(datapackage);
			return new CryptedDataPackage(encrypt(bos.toByteArray()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Benutze diese Methode um ein CryptedDataPackage zu entschlüsseln!
	 * */
	public static DataPackage decrypt(CryptedDataPackage crypteddatapackage) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(decrypt(crypteddatapackage.getBytes()));
			ObjectInput in = new ObjectInputStream(bis);
			return (DataPackage)in.readObject();
		} catch (Exception e) {
			return null;
		}
	}

	private static byte[] encrypt(byte[] data) throws Exception {
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(data);
		return encVal;
	}

	private static byte[] decrypt(byte[] encryptedData) throws Exception {
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decValue = c.doFinal(encryptedData);
		return decValue;
	}
	
	/**
	 * Mit dieser Methode wird einmalig der Sicherheitsschlüssel erstellt und in einer
	 * tempoären Variable (key) gespeichert
	 * */
	static {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec((private_key).toCharArray(), salt, 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			key = secret;
		} catch (Exception e) {}
	}
	
	/**
	 * Dieses Methode übergibt die codierte Client ID (HardWare-IDentifier)
	 * */
	public static String getHWID()
	{
		return IDHelper.getEncodedID();
	}
	
}
