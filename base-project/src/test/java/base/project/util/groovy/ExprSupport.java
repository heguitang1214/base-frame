package base.project.util.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.Eval;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ExprSupport {

    private static final Object lock = new Object();
    /**
     * 自定义的表达式解析类
     */
    private static final GroovyShell shell;

    private static Hashtable<String, Script> cache = new Hashtable<>();

    static {
        // 基于上述自定义类，实例化一个CompilerConfiguration对象
        CompilerConfiguration cfg = new CompilerConfiguration();
        cfg.setScriptBaseClass(MyBasicScript.class.getName());
        // 以此CompilerConfiguration实例为参数，实例化一个GroovyShell对象
        shell = new GroovyShell(cfg);
    }

    public static Object parseExpr(String expr) {
        Script s = getScriptFromCache(expr);
        return s.run();
    }

    public static Object parseExpr(String expr, Map<?, ?> map) {
        // 通过shell对象，解析并运行表达式。
        // 在运行前，可以通过bingding对象绑定脚本运行时的上下文数据：
        Binding binding = new Binding(map);
        Script script = getScriptFromCache(expr);
        script.setBinding(binding);
        return script.run();
    }

    private static Script getScriptFromCache(String expr) {
        if (cache.contains(expr)) {
            return cache.get(expr);
        }
        synchronized (lock) {
            if (cache.contains(expr)) {
                return cache.get(expr);
            }
            Script script = shell.parse(expr);
            cache.put(expr, script);
            return script;
        }
    }


    public static void main(String[] args) {
        // eg. get one row from db
        Map<String, Object> row = new HashMap<>();
        row.put("id", 42);
        row.put("name", "");

        //带绑定数据参数的调用方式
        System.out.println(ExprSupport.parseExpr("nvl(id,0)", row));
        System.out.println(ExprSupport.parseExpr("nvl(name,'anonymous')", row));

        //不带绑定数据参数的调用方式，这个是groovy的内置能力
        System.out.println(ExprSupport.parseExpr("1+2"));


        Map<String, Object> row1 = new HashMap<>();
        row1.put("name", "张三");
        row1.put("age", "张三1");
        System.out.println(ExprSupport.parseExpr("name == age", row1));

    }

}