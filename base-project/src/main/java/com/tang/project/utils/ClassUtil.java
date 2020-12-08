package com.tang.project.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 动态给类添加属性
 */
public class ClassUtil {

    private static final String CLASS_ATTR = "class";

    /**
     * @param object    旧的对象带值
     * @param addMap    动态需要添加的属性和属性类型
     * @param addValMap 动态需要添加的属性和属性值
     * @return 新的对象
     * @throws Exception 异常信息
     */
    public Object dynamicClass(Object object, HashMap<String, Class<?>> addMap, HashMap<String, Object> addValMap) throws Exception {
        HashMap<String, Object> attrValueMap = new HashMap<>();
        HashMap<String, Class<?>> classTypeMap = new HashMap<>();

        Class<?> type = object.getClass();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        // 获取这个类的属性，添加到classTypeMap和atrValueMap中
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!CLASS_ATTR.equals(propertyName)) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(object);
                //可以判断为 NULL不赋值
                attrValueMap.put(propertyName, result);
                classTypeMap.put(propertyName, descriptor.getPropertyType());
            }
        }
        // 需要动态添加的属性
        // todo 相同属性覆盖？
        attrValueMap.putAll(addValMap);
        classTypeMap.putAll(addMap);
        // map转换成实体对象
        DynamicBean bean = new DynamicBean(classTypeMap);
        // 赋值
        Set<String> keys = classTypeMap.keySet();
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            String key = it.next();
            bean.setValue(key, attrValueMap.get(key));
        }
        return bean.getObject();
    }

}