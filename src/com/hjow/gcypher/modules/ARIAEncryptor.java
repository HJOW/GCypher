package com.hjow.gcypher.modules;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

import org.egovframe.rte.fdl.cryptography.impl.ARIACipher;

public class ARIAEncryptor implements CypherModule {
    private static final long serialVersionUID = -4472795707416498577L;

    @Override
    public String name() {
        return "ARIA Encryptor";
    }
    
    protected String prepareKey(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    	MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] digested = digest.digest(key.getBytes("UTF-8"));
        String dgKey = Base64.getEncoder().encodeToString(digested);
        return dgKey;
    }

    @Override
    public String convert(String before, String key, Properties prop) throws Exception {
        ARIACipher cipher = new ARIACipher();
        cipher.setPassword(prepareKey(key));
        
        byte[] ciphered = cipher.encrypt(before.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(ciphered);
    }

	@Override
	public byte[] convert(byte[] before, String key, Properties prop) throws Exception {
		ARIACipher cipher = new ARIACipher();
        cipher.setPassword(prepareKey(key));
        
        return cipher.encrypt(before);
	}
}
