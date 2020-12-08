package com.tang.project.controller;

import com.tang.project.service.UserDemoService;
import com.tang.project.service.injection.SqlInjectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 注入攻击测试
 *
 * @author tang
 */
@RestController
@RequestMapping("/injection")
@Slf4j
public class InjectionController {

    @Autowired
    private SqlInjectionService sqlInjectionService;

    @Autowired
    private UserDemoService userDemoService;

    /**
     * 用户模糊搜索接口
     *
     * @param name 用户名
     */
    @PostMapping("/sql/jdbcwrong")
    public void jdbcwrong(@RequestParam("name") String name) {
        //    python sqlmap.py -u http://localhost:8100/injection/sql/jdbcwrong --data name=test
        //    python sqlmap.py -u  http://localhost:8100/injection/sql/jdbcwrong --data name=test --current-db
        //    python sqlmap.py -u  http://localhost:8100/injection/sql/jdbcwrong --data name=test --tables -D "demo"
        //    python sqlmap.py -u  http://localhost:8100/injection/sql/jdbcwrong --data name=test -D "demo" -T "userdata" --dump
        sqlInjectionService.jdbcwrong(name);
    }


    @PostMapping("/sqlin")
    public List mybatiswrong2(@RequestParam("names") String names) {
        return userDemoService.findByNames(names);
    }


    @GetMapping("/jswrong")
    public Object jswrong(@RequestParam("name") String name) {
//        http://localhost:8100/injection/jswrong?name=
        return sqlInjectionService.jswrong(name);
    }


    @GetMapping("/scriptingSandbox")
    public Object scriptingSandbox(@RequestParam("name") String name) {
        return sqlInjectionService.scriptingSandbox(name);

    }


}
