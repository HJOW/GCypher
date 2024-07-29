package com.hjow.gcypher.modules;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Properties;

import org.egovframe.rte.fdl.cryptography.impl.ARIACipher;

public class ARIADecryptor implements CypherModule {
    private static final long serialVersionUID = -4472795707416498577L;

    @Override
    public String name() {
        return "ARIA Decryptor";
    }

    @Override
    public String convert(String before, String key, Properties prop) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] digested = digest.digest(key.getBytes("UTF-8"));
        String dgKey = Base64.getEncoder().encodeToString(digested);
        digested = null;
        
        ARIACipher cipher = new ARIACipher();
        cipher.setPassword(dgKey);
        
        byte[] ciphered = cipher.decrypt(Base64.getDecoder().decode(before));
        return new String(ciphered, "UTF-8");
    }

}
