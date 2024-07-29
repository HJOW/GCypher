package com.hjow.gcypher.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/** 암/복호화 모듈을 관리하는 클래스입니다. */
public class ModuleLoader {
    private static final List<CypherModule> modules = new Vector<CypherModule>();
    static {
        register(new AESEncryptor());
        register(new AESDecryptor());
    }
    
    /** 모듈을 등록합니다. */
    public static void register(CypherModule m) {
        modules.add(m);
    }
    /** 모듈 이름들을 반환합니다. */
    public static List<String> getNames() {
        List<String> names = new ArrayList<String>();
        for(CypherModule m : modules) {
            names.add(m.name());
        }
        return names;
    }
    /** 모듈 이름으로 모듈을 찾습니다. 찾지 못하면 null 이 반환됩니다. */
    public static CypherModule get(String name) {
        for(CypherModule m : modules) {
            if(m.name().equals(name)) return m;
        }
        return null;
    }
}
