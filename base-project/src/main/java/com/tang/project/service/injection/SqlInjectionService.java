package com.tang.project.service.injection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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
        log.info("{}", jdbcTemplate.queryForList("SELECT id,name FROM userdata WHERE name LIKE '%" + name + "%'"));
    }


}
