package base.project.sqlparser;

import com.tang.common.utils.ReflectionsUtils;
import com.tang.project.entry.UserDemo;
import com.tang.project.utils.JavaScriptUtil;
import org.junit.platform.commons.util.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 */
public class Test {

    //    conditionValue: '{{org_code}}==#{SysOrgCode} && {{cash_type}}==4',
    public static String s = "{{(\\w+)}}";
    public static String s1 = "#{(\\w+)}";


    public static void main(String[] args) throws ScriptException, FileNotFoundException {

        Integer testExecute = (Integer) JavaScriptUtil.execute("2*3");
        String testExecuteForAttribute = (String) JavaScriptUtil.executeForAttribute("var value = 'a'+ 'dc'", "value");
        Boolean testExecuteForFirstAttribute = (Boolean) JavaScriptUtil.executeForFirstAttribute("var a = 6==2*3");

        // id > 5


        System.out.println(testExecute);
        System.out.println(testExecuteForAttribute);
        System.out.println(testExecuteForFirstAttribute);

        System.out.println("test over ....");

        UserDemo userDemo = new UserDemo();
        userDemo.setId("100");
        userDemo.setUserName("张三");

        Object o = ReflectionsUtils.getFieldValue(userDemo, "id");
        Object o1 = ReflectionsUtils.getFieldValue(userDemo, "userName");
//        Object o2 = ReflectionsUtils.getFieldValue(userDemo, "user_name");
        System.out.println(o);
        System.out.println(o1);
//        System.out.println(o2);


        String str = "{{org_code}}==#{SysOrgCode} && {{cash_type}}==4";
        String[] strArray = new String[]{};
        if (str.contains("&&")) {
            strArray = str.split("&&");
        } else if (str.contains("||")) {
            strArray = str.split("||");
        } else {
            strArray[0] = str;
        }

        for (String s : strArray) {
            Pattern compile = Pattern.compile(s);
            Matcher matcher = compile.matcher(s);
            if (matcher.find()) {
                String group = matcher.group();
                System.out.println(group);
            }

//            Pattern compile1 = Pattern.compile(s1);
//            Matcher matcher1 = compile1.matcher(s);
//            if (matcher1.find()) {
//                String group = matcher1.group();
//                System.out.println(group);
//            }

        }




        // {{(\w+)}}


    }


    /**
     * 1、MySBatis的SQL改写功能
     * 2、动态添加属性，修改字节码
     */
}
