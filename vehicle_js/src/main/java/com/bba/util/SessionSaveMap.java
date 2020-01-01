package com.bba.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SessionSaveMap
 * @Discription TODO
 * @Author lao li
 * @Date 2019-04-16 10:03
 * @Version 1.0
 */
public class SessionSaveMap {

    public static Map<String,String> sessionIdMap = new HashMap<String,String>();

    public static Map<String,String> getSessionIdMap() {
        return sessionIdMap;
    }


    public static void setSessionIdMap(Map<String, String> sessionIdMap) {
        sessionIdMap = sessionIdMap;
    }

}
