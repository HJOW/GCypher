package com.hjow.gcypher.modules;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Properties;

public class Digest implements CypherModule {
    private static final long serialVersionUID = -6171211831631500507L;

    @Override
    public String name() {
        return "Digest";
    }

    @Override
    public String convert(String before, String key, Properties prop) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(key);
        return Base64.getEncoder().encodeToString(digest.digest(before.getBytes("UTF-8")));
    }

}
