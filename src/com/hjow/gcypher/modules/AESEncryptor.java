package com.hjow.gcypher.modules;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptor implements CypherModule {
    private static final long serialVersionUID = -4472795707416498577L;

    @Override
    public String name() {
        return "AES Encryptor";
    }

    @Override
    public String convert(String before, String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] digested = digest.digest(key.getBytes("UTF-8"));
            String dgKey = Base64.getEncoder().encodeToString(digested);
            digested = null;
            
            if(     dgKey.length() > 32) dgKey = dgKey.substring(0, 32);
            else if(dgKey.length() > 24) dgKey = dgKey.substring(0, 24);
            else if(dgKey.length() > 16) dgKey = dgKey.substring(0, 16);
            else {
                dgKey += "1234567890ABCDEF";
                dgKey = dgKey.substring(0, 16);
            }
            
            SecretKeySpec scKeySpec = new SecretKeySpec(dgKey.getBytes("UTF-8"), "AES");
            
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, scKeySpec);
            byte[] ciphered = cipher.doFinal(before.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(ciphered);
        } catch(Exception e) {
            throw new RuntimeException("[" + e.getClass().getSimpleName() + "] " + e.getMessage(), e);
        }
    }

}
