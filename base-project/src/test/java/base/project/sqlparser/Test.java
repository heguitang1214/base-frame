package base.project.sqlparser;

import com.tang.common.utils.ReflectionsUtils;
import com.tang.project.entry.UserDemo;
import com.tang.project.utils.DataPermissionUtil;
import com.tang.project.utils.JavaScriptUtil;
import org.junit.platform.commons.util.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.crypto.Data;
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
    public static String s = "[A-Za-z_]+";


    public static void main(String[] args) throws ScriptException, FileNotFoundException {

        Integer testExecute = (Integer) JavaScriptUtil.execute("2*3");
        String testExecuteForAttribute = (String) JavaScriptUtil.executeForAttribute("var value = 'a'+ 'dc'", "value");
        Boolean testExecuteForFirstAttribute = (Boolean) JavaScriptUtil.executeForFirstAttribute("var a = 6==2*3");

        // id > 5
        Boolean testExecute1 = (Boolean) JavaScriptUtil.execute("'张三' == '张三' && 4 == 4");
        Boolean testExecute2 = (Boolean) JavaScriptUtil.execute(" 5 > 4");

        System.out.println(testExecute1);
        System.out.println(testExecute2);


        System.out.println(testExecute);
        System.out.println(testExecuteForAttribute);
        System.out.println(testExecuteForFirstAttribute);

        System.out.println("test over ....");

        System.out.println("==========================");

        UserDemo userDemo = new UserDemo();
        userDemo.setId("4");
        userDemo.setUserName("张三");


        //==========使用============
        String str = "{{userName}}==#{SysOrgCode} && {{id}}==4";
        List<String> list = DataPermissionUtil.analyticExpression(str);
        Map<String, Object> map = new HashMap<>();
        map.put("SysOrgCode", "张三");
        // 环境变量赋值
        Map<String, Object> sysVariable = new HashMap<>();
        Map<String, Object> expVariable = new HashMap<>();
        Map<String, Map<String, Object>> variable = new HashMap<>();
        for (String expression : list) {
            Object value = map.get(expression);
            if (value != null) {
                sysVariable.put(expression, value);
            } else {
                value = ReflectionsUtils.getFieldValue(userDemo, expression);
                expVariable.put(expression, value);
            }
        }
        variable.put("sys", sysVariable);
        variable.put("exp", expVariable);
        System.out.println(DataPermissionUtil.eval(str, list, variable));


    }


    // {{(\w+)}}


/**
 * 1、MySBatis的SQL改写功能
 * 2、动态添加属性，修改字节码
 */
}
