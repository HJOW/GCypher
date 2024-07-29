package com.hjow.gcypher.modules;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/** 암/복호화 모듈을 관리하는 클래스입니다. */
public class ModuleLoader {
    private static final List<CypherModule> modules = new Vector<CypherModule>();
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
    /** 리소스에서 프로그램 설정을 불러옵니다. */
    public static Properties loadPropResource(String resourceName) {
    	InputStream inp1 = null;
    	Properties  prop = new Properties();
        
        try {
            inp1 = ModuleLoader.class.getResourceAsStream(resourceName);
            if(inp1 != null) {
            	if(resourceName.toLowerCase().trim().endsWith(".xml")) prop.loadFromXML(inp1);
            	else prop.load(inp1);
                inp1.close(); inp1 = null;
            }
        } catch(Throwable t) {
            t.printStackTrace();
        } finally {
            if(inp1 != null) try { inp1.close();  } catch(Exception ignores) {}
        }
        return prop;
    }
    
    static {
    	loadResource("/bundled.txt");
    	loadResource("/list.txt");
    }
    private static void loadResource(String resourceName) {
    	InputStream       inp1 = null;
        InputStreamReader inp2 = null;
        BufferedReader    inp3 = null;
        
        try {
            inp1 = ModuleLoader.class.getResourceAsStream(resourceName);
            if(inp1 != null) {
            	inp2 = new InputStreamReader(inp1, "UTF-8");
                inp3 = new BufferedReader(inp2);
                
                String line;
                while(true) {
                    line = inp3.readLine();
                    if(line == null) break;
                    
                    try {
                        register(line.trim());
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
                
                inp3.close(); inp3 = null;
                inp2.close(); inp2 = null;
                inp1.close(); inp1 = null;
            }
        } catch(Throwable t) {
            t.printStackTrace();
        } finally {
            if(inp3 != null) try { inp3.close();  } catch(Exception ignores) {}
            if(inp2 != null) try { inp2.close();  } catch(Exception ignores) {}
            if(inp1 != null) try { inp1.close();  } catch(Exception ignores) {}
        }
    }
}
