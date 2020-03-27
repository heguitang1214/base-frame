package com.tang.file.lock.service;


/**
 * Redis分布式锁接口
 *
 * @author v_guitanghe
 */
public interface RedisLockService {

    /**
     * 加锁
     *
     * @param lockKey key值
     * @param value   value值
     * @return 是否加锁成功
     */
    boolean lock(String lockKey, String value);

    /**
     * 解锁
     *
     * @param lockKey   key值
     * @param lockValue value值
     * @return 是否解锁成功
     */
    boolean unlock(String lockKey, String lockValue);

    /**
     * 多服务器集群，使用下面的方法，代替System.currentTimeMillis()，获取redis时间，避免多服务的时间不一致问题！！！
     *
     * @return 获取redis时间
     */
    long currtTimeForRedis();
}
