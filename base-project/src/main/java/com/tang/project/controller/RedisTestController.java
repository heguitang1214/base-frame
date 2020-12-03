//package com.tang.project.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.PostConstruct;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.stream.IntStream;
//
///**
// * 测试redis缓存的收益
// */
//@RestController
//@RequestMapping("/redis")
//@Slf4j
//public class RedisTestController {
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    private AtomicInteger atomicInteger = new AtomicInteger();
//
//    /**
//     * 测试缓存雪崩
//     */
//    @PostConstruct
//    public void wrongInit() {
//        //初始化1000个城市数据到Redis，所有缓存数据有效期30秒
//        IntStream.rangeClosed(1, 1000).forEach(i -> stringRedisTemplate.opsForValue().set("city" + i, getCityFromDb(i), 30, TimeUnit.SECONDS));
//        log.info("Cache init finished");
//
//        //每秒一次，输出数据库访问的QPS
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
//            log.info("DB QPS : {}", atomicInteger.getAndSet(0));
//        }, 0, 1, TimeUnit.SECONDS);
//    }
//
//    @GetMapping("city")
//    public String city() {
//        //随机查询一个城市
//        int id = ThreadLocalRandom.current().nextInt(1000) + 1;
//        String key = "city" + id;
//        String data = stringRedisTemplate.opsForValue().get(key);
//        if (data == null) {
//            //回源到数据库查询
//            data = getCityFromDb(id);
//            if (!StringUtils.isEmpty(data)) {
//                //缓存30秒过期
//                stringRedisTemplate.opsForValue().set(key, data, 30, TimeUnit.SECONDS);
//            }
//        }
//        return data;
//    }
//
//    private String getCityFromDb(int cityId) {
//        //模拟查询数据库，查一次增加计数器加一
//        atomicInteger.incrementAndGet();
//        return "citydata" + System.currentTimeMillis();
//    }
//
//}
