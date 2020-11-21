package base.project.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Reids序列化实验
 *
 * @date 2020/11/21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Serialize {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test() {

    }


    @Test
    public void init() throws JsonProcessingException {
        redisTemplate.opsForValue().set("redisTemplate", new User("zhuye", 36));
        stringRedisTemplate.opsForValue().set("stringRedisTemplate", objectMapper.writeValueAsString(new User("zhuye", 36)));
    }


}
