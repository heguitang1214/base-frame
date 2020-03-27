package com.tang.file.lock.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Random;

/**
 * Redis分布式的实现
 *
 * @author v_guitanghe
 */
@Service
public class RedisLockServiceImpl implements RedisLockService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String UNLOCK_SCRIPT =
            "if redis.call('get',KEYS[1]) == ARGV[1]\n"
                    + "then\n"
                    + "    return redis.call('del',KEYS[1])\n"
                    + "else\n"
                    + "    return 0\n"
                    + "end";

    /**
     * 默认请求锁的超时时间(ms 毫秒)
     */
    private static final long TIME_OUT = 100;

    /**
     * 存储到redis中的锁标志
     */
    private static final String LOCKED = "HR-818-LOCKED";

    /**
     * 默认锁的有效时间(s)
     */
    public static final int EXPIRE = 60;


    /**
     * @param lockKey key值
     * @param value   value值
     * @return 是否加锁成功
     */
    @Override
    public boolean lock(String lockKey, String value) {
        // 请求锁的超时时间/获取锁的超时时间
        long timeout = 2 * 1000;
        final Random random = new Random();
        // 随机生成一个value
//        String value = UUID.randomUUID().toString();
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        RedisConnection redisConnection = connectionFactory.getConnection();

        long end = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < end) {
            if (redisConnection.setNX(lockKey.getBytes(), value.getBytes())) {
                // 对lockkey设置超时时间，为的是避免死锁问题    100：设置锁的超时时间 ms
//                redisConnection.pExpire(lockKey.getBytes(), 10);
                redisConnection.expire(lockKey.getBytes(), 10);
                // 释放连接
                RedisConnectionUtils.releaseConnection(redisConnection, connectionFactory);
                return true;
            }
            // 返回-1代表key没有设置超时时间，为key设置一个超时时间
//            System.out.println("超时时间: " + redisConnection.ttl(lockKey.getBytes()));
            if (redisConnection.ttl(lockKey.getBytes()) == -1) {
                redisConnection.expire(lockKey.getBytes(), 10);
            }
            // 获取锁失败时，应该在随机延时后进行重试，避免不同客户端同时重试导致谁都无法拿到锁的情况出现
            // 睡眠10毫秒后继续请求锁
            try {
                Thread.sleep(10, random.nextInt(50000));
            } catch (InterruptedException e) {
                logger.error("获取分布式锁休眠被中断：", e);
            }
        }
        RedisConnectionUtils.releaseConnection(redisConnection, connectionFactory);
        return false;
    }


    @Override
    public boolean unlock(String lockKey, String lockValue) {
        byte[][] keysAndArgs = new byte[2][];
        keysAndArgs[0] = lockKey.getBytes(Charset.forName("UTF-8"));
        keysAndArgs[1] = lockValue.getBytes(Charset.forName("UTF-8"));

        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        RedisConnection redisConnection = connectionFactory.getConnection();
        try {
            return redisConnection.scriptingCommands().eval(
                    UNLOCK_SCRIPT.getBytes(Charset.forName("UTF-8")), ReturnType.BOOLEAN, 1, keysAndArgs);
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, connectionFactory);
        }
    }


    @Override
    public long currtTimeForRedis() {
        return redisTemplate.getConnectionFactory().getConnection().time();
    }
}
