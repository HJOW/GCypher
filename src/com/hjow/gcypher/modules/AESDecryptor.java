package com.hjow.gcypher.modules;

import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESDecryptor extends AESEncryptor {
    private static final long serialVersionUID = -4472795707416498577L;

    @Override
    public String name() {
        return "AES Decryptor";
    }
    
    @Override
    public String convert(String before, String key, Properties prop) throws Exception {
        SecretKeySpec scKeySpec = prepareKey(key);
        
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, scKeySpec);
        byte[] ciphered = cipher.doFinal(Base64.getDecoder().decode(before));
        return new String(ciphered, "UTF-8");
    }

	@Override
	public byte[] convert(byte[] before, String key, Properties prop) throws Exception {
        SecretKeySpec scKeySpec = prepareKey(key);
        
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, scKeySpec);
        return cipher.doFinal(before);
	}

}
