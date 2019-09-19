package com.winstar.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by zl on 2019/9/10
 */
@Service
public class OilRedisTools {

    @Resource(name = "oilRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取list列表里面元素数量
     */
    public Long size(String key){
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 判断缓存中是否有对应的value
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除对应的value
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * set存储
     */
    public void addSet(String key, Object... values){
        SetOperations<String, Object> operations = redisTemplate.opsForSet();
        operations.add(key, values);
    }

    /**
     * set随机取值
     */
    public Object getRandomKeyFromSet(String key){
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * set随机取值
     */
    public Long getSetSize(String key){
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 是否能把键值放到换缓存中，能放入则同时设置有效时间
     */
    public boolean setIfAbsent(final String key, long times){
        boolean result = redisTemplate.opsForValue().setIfAbsent(key,key);
        if(result){
            redisTemplate.opsForValue().getOperations().expire(key, times, TimeUnit.SECONDS);
        }
        return result;
    }

}
