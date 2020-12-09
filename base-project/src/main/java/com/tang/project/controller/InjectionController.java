package com.tang.project.controller;

import com.tang.project.entry.UserDemo;
import com.tang.project.service.UserDemoService;
import com.tang.project.service.injection.SqlInjectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


    @PostMapping("/sql/in")
    public List mybatiswrong2(@RequestParam("names") String names) {
        return userDemoService.findByNames(names);
    }


    @GetMapping("/js/wrong")
    public Object jswrong(@RequestParam("name") String name) {
//        http://localhost:8100/injection/jswrong?name=
        return sqlInjectionService.jswrong(name);
    }


    @GetMapping("/js/scriptingSandbox")
    public Object scriptingSandbox(@RequestParam("name") String name) {
        return sqlInjectionService.scriptingSandbox(name);

    }


    /**
     * 显示xss页面
     *
     * @param modelMap modelMap
     * @return 字符串
     */
    @GetMapping("/xss/index")
    public String index(ModelMap modelMap) {
        //查数据库
        UserDemo user = userDemoService.getById("100");
        if (user == null) {
            user = new UserDemo();
        }
        //给View提供Model
        modelMap.addAttribute("username", user.getUserName());
        return "xss";
    }


    /**
     * 保存用户信息
     */
    @PostMapping("/xss/save")
    public String save(@RequestParam("username") String username, HttpServletRequest request) {
        UserDemo user = new UserDemo();
        user.setId("300");
        user.setUserName(username);
        userDemoService.save(user);
        //保存完成后重定向到首页
        return "redirect:/xss/";
    }


    /**
     * 服务端读取Cookie
     */
    @GetMapping("/xss/readCookie")
    @ResponseBody
    public String readCookie(@CookieValue("test") String cookieValue) {
        return cookieValue;
    }

    /**
     * 服务端写入Cookie
     */
    @GetMapping("/xss/writeCookie")
    @ResponseBody
    public void writeCookie(@RequestParam("httpOnly") boolean httpOnly, HttpServletResponse response) {
        Cookie cookie = new Cookie("test", "zhuye");
        //根据httpOnly入参决定是否开启HttpOnly属性
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

}
