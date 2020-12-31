package org.jeecg.modules.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtil {
    /**
     * 将对象装换为map
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     *
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     *

     */
    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     *
     * @param maps
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        List<T> list = Lists.newArrayList();
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = maps.size(); i < size; i++) {
                map = maps.get(i);
                bean = clazz.newInstance();
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }

    public static List<Map<String, Object>> toListMap(String json){
        List<Object> list =JSON.parseArray(json);
        System.out.println(json);
        List< Map<String,Object>> listw = new ArrayList<Map<String,Object>>();
        for (Object object : list){
            Map<String,Object> ageMap = new HashMap<String,Object>();
            Map <String,Object> ret = (Map<String, Object>) object;//取出list里面的值转为map
            listw.add(ret);
        }
        return listw;

    }


    public static void main(String[] args) {
        String s = "{\n\t\"ipAddress\":\t\"0.0.0.0\",\n\t\"portNo\":\t80,\n\t\"dateTime\":\t\"2020-05-20T16:28:33+00:00\",\n\t\"activePostCount\":\t1,\n\t\"eventType\":\t\"4GSafetyHelmetInfo\",\n\t\"eventState\":\t\"inactive\",\n\t\"eventDescription\":\t\"4GSafetyHelmetInfo\",\n\t\"deviceID\":\t\"E43690957\",\n\t\"FourGSafetyHelmetInfo\":\t{\n\t\t\"Heartbeat\":\t{\n\t\t\t\"helmetStatus\":\tfalse,\n\t\t\t\"beaconId\":\t\"0\",\n\t\t\t\"GPS\":\t{\n\t\t\t\t\"latitude\":\t0,\n\t\t\t\t\"longitude\":\t0\n\t\t\t},\n\t\t\t\"powerPercent\":\t90,\n\t\t\t\"workStatus\":\t\"on\",\n\t\t\t\"RSSISignalStrength\":\t\"weak\",\n\t\t\t\"isAbnormalStationary\":\ttrue,\n\t\t\t\"isSOS\":\tfalse\n\t\t}\n\t}\n}";
        JSONObject object = JSON.parseObject(s);
        System.out.println(object.get("eventState"));

    }


}
