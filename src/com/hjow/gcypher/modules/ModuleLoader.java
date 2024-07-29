package com.hjow.gcypher.modules;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/** 암/복호화 모듈을 관리하는 클래스입니다. */
public class ModuleLoader {
    private static final List<CypherModule> modules = new Vector<CypherModule>();
    static {
        InputStream       inp1 = null;
        InputStreamReader inp2 = null;
        BufferedReader    inp3 = null;
        
        try {
            inp1 = ModuleLoader.class.getResourceAsStream("/list.txt");
            inp2 = new InputStreamReader(inp1, "UTF-8");
            inp3 = new BufferedReader(inp2);
            
            String line;
            while(true) {
                line = inp3.readLine();
                if(line == null) break;
                
                register(line.trim());
            }
        } catch(Throwable t) {
            t.printStackTrace();
        } finally {
            if(inp3 != null) try { inp3.close();  } catch(Exception ignores) {}
            if(inp2 != null) try { inp2.close();  } catch(Exception ignores) {}
            if(inp1 != null) try { inp1.close();  } catch(Exception ignores) {}
        }
    }
    /** 모듈을 등록합니다. */
    @SuppressWarnings("unchecked")
    public static void register(String moduleClass) {
        try {
            Class<? extends CypherModule> moduleClassObj = (Class<? extends CypherModule>) Class.forName(moduleClass);
            modules.add(moduleClassObj.newInstance());
        } catch(Throwable t) {
            t.printStackTrace();
        }
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
