package com.tang.project.controller;

import com.tang.project.service.injection.SqlInjectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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

    /**
     * 用户模糊搜索接口
     *
     * @param name 用户名
     */
    @PostMapping("jdbcwrong")
    public void jdbcwrong(@RequestParam("name") String name) {
        sqlInjectionService.jdbcwrong(name);
    }


}
