package com.tang.project.controller;

import com.tang.project.dto.UserDemoDto;
import com.tang.project.entry.UserDemo;
import com.tang.project.service.UserDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserDemoController {

    @Autowired
    private UserDemoService userDemoService;

    @GetMapping("/list")
    public List<UserDemo> list() {
        return userDemoService.test();
    }

    @GetMapping("/sqlTest")
    public List<UserDemo> sqlTest() {
        return userDemoService.sqlTest();
    }

}
