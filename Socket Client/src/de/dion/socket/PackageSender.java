package de.dion.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import de.dion.socket.objects.DataPackage;
import de.dion.socket.utils.Crypter;
import de.dion.socket.utils.IDHelper;

public class PackageSender {
	
	private final InetSocketAddress address;
	 //TEst for Chaot programm
	
	public PackageSender(InetSocketAddress adress) {
		this.address = adress;
	}
	
	public InetSocketAddress getAddress() {
		return address;
	}

	/**
	 * Mit dieser Methode kannst du ein DataPackage an
	 * den Server senden.
	 * Das DataPackage wird von ganz allein verschlüsselt und
	 * dann übertragen.
	 * */
	public boolean sendDataPackage(DataPackage pack)
	{
		try {
			Socket temp = new Socket();
			temp.connect(address, 2000);
			ObjectOutputStream os = new ObjectOutputStream(temp.getOutputStream());
			os.writeObject(Crypter.encrypt(pack));
			
			os.close();
			temp.close();
			return true;
		} catch (Exception e) {}
		return false;
	}
	
}
