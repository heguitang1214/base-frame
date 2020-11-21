package com.tang.project.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis配置
 *
 * @date 2020/11/21
 */
public class RedisConfig {

    /**
     * redis序列化方式
     * key为String；value为json格式
     *
     * @param redisConnectionFactory redis连接工厂
     * @param <T>                    泛型类型
     * @return RedisTemplate模板
     */
    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 自定义Value的序列化方式
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //把类型信息作为属性写入Value
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // 使用 RedisSerializer.json 的快捷方法或者自定义Jackson2JsonRedisSerializer
//        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}
