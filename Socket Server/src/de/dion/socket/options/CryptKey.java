package de.dion.socket.options;

public interface CryptKey {
	
	/** Dies ist Der Key, mit dem die DataPackages verschlüsselt werden.
	 *  Pass auf, dass du ihn niemanden zeigst!
	 *  Der Schlüssel muss immer 16 Zeichen lang sein! */
	final String private_key = "AAABAFBqI2Sw1ws6gEPUKKspVwpdl746nmp+jLXBGCAf5iK4Us6/dIa/hmWw8g3iVqi3cLdw8sbjm0gztoCMO3b5UUUdpeCPXIMjtZ/o5ozpRXy4sEPd8bd8BNzIAInF0fHDIX4hseni0i6kvAfDEsZbV87Fb8cnkQsXNw1OxhHXc7uN/xzryNfqF78zwal4BJ7sfLDzOsWStBpRhupe05qhO8jo25mKbzMs3BghPu3qlcNuOVPVBKU4yHSgURkT";
	
	/**
	 * Das ist der Salt wert, welcher verhindert, dass die Verschlüsselung
	 * einfach durch Bruteforcen geknackt wird.
	 * Diesen Wert darfst du anderen Leuten zeigen.
	 * */
	final byte[] salt = new byte[] {
		-69,-115,-32,-107,-111,109,-109,116,-32,118,-105,-101,108,109,101,104,-114,32,122,117,32,101,-105,-110,-101,109,32,-76,-105,101,98,-101,115,-115,112,105,-101,108,-32,-122,-119,105,-115,-99,104,-101,110,32
	};
	
}
