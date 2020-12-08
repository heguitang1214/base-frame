package base.project.util.js;

import com.tang.project.utils.JavaScriptUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.script.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析JS表达式式，并执行
 */
public class JavaScriptTest {
    public static void main(String[] args) throws Exception {
//        scriptEvalTest();

//        replaceValue();

//        simpleBindings();

        includes();
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

    public static void simpleBindings() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Map<String, Object> map = new HashMap<>();

        String str = "['manage_customManager','poros-manage_customDevelop'].indexOf[code]";

        map.put("code", "fox");

//        Bindings bind = engine.createBindings();
//        bind.put("code", "GSP");
//        engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);

        Object result1 = engine.eval(str, new SimpleBindings(map));
        System.out.println("结果类型:" + result1.getClass().getName() + ",计算结果:" + result1);

    }


    public static void includes() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        String str = "['manage_customManager','poros-manage_customDevelop'].includes[code]";
        // 支持
        str = "'[poros-manage_customManager, poros-manage_customDevelop]'.includes('code')";
        // 支持
        str = "'[manage_customManager, manage_customDevelop, 3]'.includes('customDevelop1')";
        // 支持 字符传like
        str = "'poros-manage_customManager,aaabbb'.includes(',a')";
        // 支持 in NASHORN_POLYFILL_ARRAY_PROTOTYPE_INCLUDES
//        str = "!['poros-manage_customManager', 'poros-manage_customDevelop'].includes('code')";
        str = "![1, 2].includes(2)";

        engine.eval(NASHORN_POLYFILL_ARRAY_PROTOTYPE_INCLUDES);
        Object result1 = engine.eval(str);
        System.out.println("结果类型:" + result1.getClass().getName() + ",计算结果:" + result1);

    }


    //=========================测试对includes的兼容
    // Copied from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/includes#Polyfill
    // 字符串处理
    public static final String NASHORN_POLYFILL_STRING_PROTOTYPE_INCLUDES = "if (!String.prototype.includes) { Object.defineProperty(String.prototype, 'includes', { value: function(search, start) { if (typeof start !== 'number') { start = 0 } if (start + search.length > this.length) { return false } else { return this.indexOf(search, start) !== -1 } } }) }";
    // Copied from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/includes#Polyfill
    // 数组的处理
    public static final String NASHORN_POLYFILL_ARRAY_PROTOTYPE_INCLUDES = "if (!Array.prototype.includes) { Object.defineProperty(Array.prototype, 'includes', { value: function(valueToFind, fromIndex) { if (this == null) { throw new TypeError('\"this\" is null or not defined'); } var o = Object(this); var len = o.length >>> 0; if (len === 0) { return false; } var n = fromIndex | 0; var k = Math.max(n >= 0 ? n : len - Math.abs(n), 0); function sameValueZero(x, y) { return x === y || (typeof x === 'number' && typeof y === 'number' && isNaN(x) && isNaN(y)); } while (k < len) { if (sameValueZero(o[k], valueToFind)) { return true; } k++; } return false; } }); }";

    @Test
    public void testStringIncludesWithPolyfill() throws Exception {
        runScript(NASHORN_POLYFILL_STRING_PROTOTYPE_INCLUDES, "'[1, 2, 3]'.includes(2)");
    }

    @Test(expected = javax.script.ScriptException.class)
    public void testStringIncludesWithoutPolyfill() throws Exception {
        runScript(null, "'[1, 2, 3]'.includes(2)");
    }

    @Test
    public void testArrayIncludesWithPolyfill() throws Exception {
        runScript(NASHORN_POLYFILL_ARRAY_PROTOTYPE_INCLUDES, "[1, 2, 3].includes(2)");
    }

    @Test(expected = javax.script.ScriptException.class)
    public void testArrayIncludesWithoutPolyfill() throws Exception {
        runScript(null, "[1, 2, 3].includes(2)");
    }

    private void runScript(final String polyfill, final String booleanExpression) throws ScriptException {
        final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
//        final ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
        Assert.assertNotNull(scriptEngine);
        if (null != polyfill) {
            scriptEngine.eval(polyfill);
        }
        final Object booleanExpressionResult = scriptEngine.eval(booleanExpression);    // returns Boolean object
        Assert.assertNotNull(booleanExpressionResult);
        Assert.assertEquals(booleanExpressionResult.getClass(), Boolean.class);
        System.out.println(booleanExpression + " = " + booleanExpressionResult.toString());
    }

}