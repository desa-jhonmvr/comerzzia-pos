/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author Gabriel Simbania
 */
public class JsonUtil {

    private static final Logger log = Logger.getMLogger(JsonUtil.class);

    /**
     *
     * @author Gabriel Simbania
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {

        String json = null;
        Gson gson = new Gson();
        json = gson.toJson(object);

        return json;
    }

    /**
     * @author Gabriel Simbania
     * @param <T>
     * @param json
     * @param clsReturn
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> clsReturn) {

        Object object = null;
        Gson gson = new Gson();
        object = gson.fromJson(json, clsReturn);

        return clsReturn.cast(object);
    }
    
    /**
     * @author Gabriel Simbania
     * @param json
     * @param element
     * @return 
     */
    public static String getElementJson(String json, String element){
        
       Gson gson = new Gson();
       JsonObject jobj = gson.fromJson(json, JsonObject.class);
       return jobj.get(element).getAsString();
    }

}
