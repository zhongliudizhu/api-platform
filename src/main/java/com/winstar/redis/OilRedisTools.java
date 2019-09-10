package com.winstar.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Created by zl on 2019/9/10
 */
@Service
public class OilRedisTools {

    @Resource(name = "oilRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 从右边向list列表里面添加元素
     */
    public void rightPushAll(String key, Collection values){
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 获取list列表里面元素数量
     */
    public Long size(String key){
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 从左边获取list里面的第一个元素并列表中移除
     */
    public Object leftPop(String k){
        return redisTemplate.opsForList().leftPop(k);
    }

}
