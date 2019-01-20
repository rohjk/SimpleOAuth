package com.jk.simpleplatform;

import com.jk.simpleplatform.idp.IdpType;

import java.util.HashMap;
import java.util.Map;

public class SimpleAuthprovider {
    private Map<IdpType, String> authMap;

    private static SimpleAuthprovider instance;

    public static SimpleAuthprovider getInstance(){
        if(instance == null){
            instance = new SimpleAuthprovider();
        }
        return instance;
    }

    SimpleAuthprovider(){
        authMap = new HashMap<>();
    }

    public String getServerId(IdpType type){
        return authMap.get(type);
    }

    public void addServerId(IdpType type, String serverId){
        authMap.put(type,serverId);
    }

}
