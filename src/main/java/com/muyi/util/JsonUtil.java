
package com.muyi.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @Author: muyi
 * @Date: Created in 16:46 2017/11/7
 * @Description:
 */
public class JsonUtil {

    public static String toPrettyJson(Object o){
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(o);
    }

    public static String toJson(Object o){
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
        Gson gson = gsonBuilder.create();
        return gson.toJson(o);
    }

}
