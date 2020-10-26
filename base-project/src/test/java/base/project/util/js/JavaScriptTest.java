package base.project.util.js;

import com.tang.project.utils.JavaScriptUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 解析JS表达式式，并执行
 */
public class JavaScriptTest {
    public static void main(String[] args) throws Exception {
        scriptEvalTest();

        replaceValue();
    }

    private static void scriptEvalTest() throws ScriptException {
        Integer intTest = (Integer) JavaScriptUtil.execute("2*3");
        String StrTest = (String) JavaScriptUtil.executeForAttribute("var value = 'a'+ 'dc'", "value");
        Boolean bTest = (Boolean) JavaScriptUtil.executeForFirstAttribute("var a = 6==2*3");

        System.out.println(intTest);
        System.out.println(StrTest);
        System.out.println(bTest);

        System.out.println("==============================================");
        // id > 5
        Boolean testExecute1 = (Boolean) JavaScriptUtil.execute("'张三' == '张三' && 4 == 4");
        Boolean testExecute2 = (Boolean) JavaScriptUtil.execute(" 5 > 4");

        System.out.println(testExecute1);
        System.out.println(testExecute2);

        System.out.println("test over ....");
    }

    public static void replaceValue() throws ScriptException {
        String str = "(a or b) and c";
        str = str.replaceAll("or", "||");
        str = str.replaceAll("and", "&&");
        System.out.println(str);
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("a", true);
        engine.put("b", false);
        engine.put("c", true);
        Object result = engine.eval(str);
        System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:" + result);

        str = "org_code==SysOrgCode && cash_type==4";
        ScriptEngineManager manager1 = new ScriptEngineManager();
        ScriptEngine engine1 = manager1.getEngineByName("js");
        engine1.put("org_code", "张三");
        engine1.put("SysOrgCode", "张三");
        engine1.put("cash_type", 4);
        Object result1 = engine1.eval(str);
        System.out.println("结果类型:" + result1.getClass().getName() + ",计算结果:" + result1);

    }
}