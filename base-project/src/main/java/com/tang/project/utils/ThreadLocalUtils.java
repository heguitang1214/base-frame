package com.tang.project.utils;

import com.tang.project.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ThreadLocalUtils {
    private static ThreadLocal threadLocal = new ThreadLocal();

    private static final String KEY_USER = "user";

    private static final String KEY_USER_MENU_ID = "USER_MENU_ID";

    private  static final Logger logger = LoggerFactory.getLogger(ThreadLocalUtils.class);

    static{
        threadLocal.set(new HashMap<String,Object>());
    }



    public static void set(String key , Object object){
        HashMap<String, Object> map = getMap();
        map.put(key, object);
        logger.trace("设置到本地线程缓存中，key:{} value:{}" , key , object);
    }

    private static HashMap<String ,Object> getMap(){
        HashMap<String,Object> map = ( HashMap<String,Object> )threadLocal.get();
        if (map == null) {
            map = new HashMap<String,Object>();
            threadLocal.set(map);
        }
        return map;
    }

    public static void setUser(UserDto userDto) {
        set(KEY_USER , userDto);
    }

    public static UserDto getUser(){
        return get(KEY_USER);
    }

    public static Integer getCompanyId(){
        UserDto u = getUser();
        return u == null ? null : u.getCompanyId();
    }

    public static <T> T  get(String key) {
        HashMap<String,Object> map = ( HashMap<String,Object> )threadLocal.get();
        if (map == null) {
            return null;
        }
        return (T)map.get(key);
    }

    public static void remove(){
        Object obj = threadLocal.get();
        logger.trace("开始从到本地线程缓存中移除!，计划移除对象：{}" , obj);
        threadLocal.remove();
        logger.trace("已经从到本地线程缓存中移除!，移除后线程对象:" ,threadLocal.get());
    }

    public static void setMenuId(Integer menuId) {
        set(KEY_USER_MENU_ID , menuId);
    }

    public static Integer getMenuId() {
        return  get(KEY_USER_MENU_ID);
    }
}
