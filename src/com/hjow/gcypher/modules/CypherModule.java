package com.hjow.gcypher.modules;

import java.io.Serializable;
import java.util.Properties;

/** 문자열 변환을 지원하는 클래스임을 표시할 수 있는 인터페이스 */
public interface CypherModule extends Serializable {
    public String name();
    public String convert(String before, String key, Properties prop);
}
