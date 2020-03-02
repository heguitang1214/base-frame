package com.tang.counter.controller;

import com.tang.counter.dto.RequestInfo;
import com.tang.counter.service.impl.Metrics;
import com.tang.counter.vo.UserVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 应用场景：统计下面两个接口 (注册和登录）的响应时间和访问次数
 * 接口调用测试
 *
 * @author tang
 */
@RestController
@RequestMapping("/counter/user")
public class UserController {

    private MetricsCollector metricsCollector = new MetricsCollector();

    private Metrics metrics = new Metrics();

    public UserController() {
        metrics.startRepeatedReport(60, TimeUnit.SECONDS);
    }

    @RequestMapping("/register")
    @ResponseBody
    public void register(UserVo user) {
        long startTimestamp = System.currentTimeMillis();

        Random random = new Random(10000);
        try {
            Thread.sleep(random.nextLong());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long respTime = System.currentTimeMillis() - startTimestamp;

        // 可以通过AOP切面来统一处理
        RequestInfo requestInfo = new RequestInfo("register", respTime, startTimestamp);
        metricsCollector.recordRequest(requestInfo);
    }

    @RequestMapping("/login")
    @ResponseBody
    public UserVo login(String telephone, String password) {
        long startTimestamp = System.currentTimeMillis();
        metrics.recordTimestamp("login", startTimestamp);
        //...
        long respTime = System.currentTimeMillis() - startTimestamp;
        metrics.recordResponseTime("login", respTime);
        return null;
    }
}