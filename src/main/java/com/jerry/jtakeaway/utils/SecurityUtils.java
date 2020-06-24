package com.jerry.jtakeaway.utils;

import java.util.HashMap;
import java.util.Map;

public class SecurityUtils {
    private Map<String,String> securityMap = new HashMap<>();

    private static SecurityUtils instance;

    public static synchronized SecurityUtils getInstance() {
        if (instance == null) instance = new SecurityUtils();
        return instance;
    }


    public void add(String key,String value){
        securityMap.put(key,value);
    }

    public String getValue(String key){
        return securityMap.get(key);
    }

    public String remove(String key){
        return securityMap.remove(key);
    }
}
