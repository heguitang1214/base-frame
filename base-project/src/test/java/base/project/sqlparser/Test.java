package base.project.sqlparser;

import com.tang.common.utils.ReflectionsUtils;
import com.tang.project.entry.UserDemo;
import com.tang.project.utils.DataPermissionUtil;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 */
public class Test {

    //    conditionValue: '{{org_code}}==#{SysOrgCode} && {{cash_type}}==4',
    public static String s = "[A-Za-z_]+";


    public static void main(String[] args) throws ScriptException, FileNotFoundException {
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
