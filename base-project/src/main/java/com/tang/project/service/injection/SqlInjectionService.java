package com.tang.project.service.injection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SqlInjectionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 程序启动时进行表结构和数据初始化
     */
    @PostConstruct
    public void init() {
        //删除表
        jdbcTemplate.execute("drop table IF EXISTS `userdata`;");
        //创建表，不包含自增ID、用户名、密码三列
        jdbcTemplate.execute("create TABLE `userdata` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(255) NOT NULL,\n" +
                "  `password` varchar(255) NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
        //插入两条测试数据
        jdbcTemplate.execute("INSERT INTO `userdata` (name,password) VALUES ('test1','haha1'),('test2','haha2')");
        log.info("测试SQL注入的数据，初始化完成");
    }


    public void jdbcwrong(String name) {
        //采用拼接SQL的方式把姓名参数拼到LIKE子句中
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT id,name FROM userdata WHERE name LIKE '%" + name + "%'");
//        log.info("{}", jdbcTemplate.queryForList("SELECT id,name FROM userdata WHERE name LIKE '%" + name + "%'"));
        log.info("SQL注入分析，请求参数name为：{}，查询的结果为：{}", name, maps);
    }


    public Object jswrong(String name) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        //获得JavaScript脚本引擎
        ScriptEngine jsEngine = scriptEngineManager.getEngineByName("js");

        try {
            //通过eval动态执行JavaScript脚本，这里name参数通过字符串拼接方式混入JavaScript代码
            log.info("执行ScriptEngine，传入的参数为：{}", name);
            // 存在安全问题
//            return jsEngine.eval(String.format("var name='%s'; name=='admin'?1:0;", name));

            // 外部传入的参数
            Map<String, Object> parm = new HashMap<>();
            parm.put("name", name);
            //name参数作为绑定传给eval方法，而不是拼接JavaScript代码
            return jsEngine.eval("name=='admin'?1:0;", new SimpleBindings(parm));
        } catch (ScriptException e) {
            log.error("js引擎执行出现异常，异常信息为：", e);
            e.printStackTrace();
        }
        return null;
    }


    public Object scriptingSandbox(String name) {
        // 使用沙箱执行脚本
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        //获得JavaScript脚本引擎
        ScriptEngine jsEngine = scriptEngineManager.getEngineByName("js");

        ScriptingSandbox scriptingSandbox;
        try {
            scriptingSandbox = new ScriptingSandbox(jsEngine);
            return scriptingSandbox.eval(String.format("var name='%s'; name=='admin'?1:0;", name));
        } catch (InstantiationException e) {
            log.error("沙箱环境执行js代码出错，异常信息为：", e);
            e.printStackTrace();
        }
        return null;
    }
}
