package com.hjow.gcypher.modules;

import java.util.Base64;
import java.util.Properties;

import org.egovframe.rte.fdl.cryptography.impl.ARIACipher;

public class ARIADecryptor extends ARIAEncryptor {
    private static final long serialVersionUID = -4472795707416498577L;

    @Override
    public String name() {
        return "ARIA Decryptor";
    }

    @Override
    public String convert(String before, String key, Properties prop) throws Exception {
        ARIACipher cipher = new ARIACipher();
        cipher.setPassword(prepareKey(key));
        
        byte[] ciphered = cipher.decrypt(Base64.getDecoder().decode(before));
        return new String(ciphered, "UTF-8");
    }

	@Override
	public byte[] convert(byte[] before, String key, Properties prop) throws Exception {
		ARIACipher cipher = new ARIACipher();
        cipher.setPassword(prepareKey(key));
        
        return cipher.decrypt(before);
	}

}
