package com.tang.project.utils;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DynamicBean {
    /**
     * 动态生成的类
     */
    private Object object = null;

    /**
     * 存放属性名称以及属性的类型
     */
    private BeanMap beanMap = null;

    public DynamicBean() {
        super();
    }

    public DynamicBean(Map<String, Class<?>> propertyMap) {
        this.object = generateBean(propertyMap);
        this.beanMap = BeanMap.create(this.object);
    }

    /**
     * 利用属性名和属性类型，生成一个新的Bean
     *
     * @param propertyMap 类属性Map，属性名-属性类型
     * @return 新的bean对象
     */
    private Object generateBean(Map<String, Class<?>> propertyMap) {
        BeanGenerator generator = new BeanGenerator();
        Set<String> keySet = propertyMap.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext(); ) {
            String key = i.next();
            generator.addProperty(key, propertyMap.get(key));
        }
        return generator.create();
    }

    /**
     * 给bean属性赋值
     *
     * @param property 属性名
     * @param value    值
     */
    public void setValue(Object property, Object value) {
        beanMap.put(property, value);
    }

    /**
     * 通过属性名得到属性值
     *
     * @param property 属性名
     * @return 值
     */
    public Object getValue(String property) {
        return beanMap.get(property);
    }

    /**
     * 得到该实体bean对象
     *
     * @return 该实体bean对象
     */
    public Object getObject() {
        return this.object;
    }
}