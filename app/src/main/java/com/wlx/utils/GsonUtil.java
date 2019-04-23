package com.wlx.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/30.
 */

public class GsonUtil {

    private static final String TAG = "GsonUtil";
    private volatile static Gson gson = new GsonBuilder().serializeNulls().create();

    /**
     *
     * @param json
     * @param typeOfT  new TypeToken<List<NewsComment>>(){}.getType()
     * @return
     */
    public static List toList(String json, Type typeOfT){
        List list = null;
        try {
            list = gson.fromJson(json, typeOfT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list==null ? new ArrayList() : list;
    }

    /**
     *
     * @param json
     * @param typeOfT new TypeToken<List<NewsComment>>(){}.getType()
     * @return
     */
    public static String toJson(List json, Type typeOfT){
        return gson.toJson(json, typeOfT);
    }


    /**
     *
     * @param json
     * @param typeOfT  new TypeToken<List<NewsComment>>(){}.getType()
     * @return
     */
    public static HashMap toMap(String json, Type typeOfT){
        HashMap map = gson.fromJson(json, typeOfT);
        return map==null ? new HashMap() : map;
    }

    /**
     *
     * @param json
     * @param typeOfT new TypeToken<Map>(){}.getType()
     * @return
     */
    public static String toJson(Map json, Type typeOfT){
        return gson.toJson(json, typeOfT);
    }

    /**
     *
     * @param json
     * @param cls
     * @return
     */
    public static Object fromJSON(String json, Class<?> cls) {
        return gson.fromJson(json, cls);
    }

    /**
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        String toJson = gson.toJson(obj);
        return toJson;
    }
}
