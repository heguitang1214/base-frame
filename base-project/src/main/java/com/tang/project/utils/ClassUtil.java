package com.tang.project.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ClassUtil {

    /**
     * @param object    旧的对象带值
     * @param addMap    动态需要添加的属性和属性类型
     * @param addValMap 动态需要添加的属性和属性值
     * @return 新的对象
     * @throws Exception 异常信息
     */
    public Object dynamicClass(Object object, HashMap<String, Class<?>> addMap, HashMap<String, Object> addValMap) throws Exception {
        HashMap<String, Object> atrValueMap = new HashMap<>();
        HashMap<String, Class<?>> classTypeMap = new HashMap<>();

        Class<?> type = object.getClass();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        // 获取这个类的属性，添加到classTypeMap和atrValueMap中
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(object);
                //可以判断为 NULL不赋值
                atrValueMap.put(propertyName, result);
                classTypeMap.put(propertyName, descriptor.getPropertyType());
            }
        }
        // 需要动态添加的属性
        atrValueMap.putAll(addValMap);
        classTypeMap.putAll(addMap);
        // map转换成实体对象
        DynamicBean bean = new DynamicBean(classTypeMap);
        // 赋值
        Set<String> keys = classTypeMap.keySet();
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            String key = it.next();
            bean.setValue(key, atrValueMap.get(key));
        }
        return bean.getObject();
    }

}