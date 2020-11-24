package base.project.util.groovy;

import groovy.lang.Script;

import java.lang.reflect.Method;

/**
 * 继承groovy.lang.Script，定义自己的表达式解析类
 */
public class MyBasicScript extends Script {

    @Override
    public Object run() {
        //show usage
        System.out.println("=========MyBasicScript的run()===============");
        Method[] methods = MyBasicScript.class.getDeclaredMethods();
        StringBuilder sb = new StringBuilder();
        for (Method method : methods) {
            sb.append(method);
        }

        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 实现具体的解析方法
     *
     * @param str 表达式
     * @param val 变量
     * @return object
     */
    public static Object nvl(Object str, Object val) {
        return str == null || "".equals(str) ? val : str;
    }

}