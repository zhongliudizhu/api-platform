package com.winstar.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
     * set存储 有效期
     */
    public void addSetExpire(String key, Long times, Object... values){
        SetOperations<String, Object> operations = redisTemplate.opsForSet();
        operations.add(key, values);
        if(!ObjectUtils.isEmpty(times)){
            operations.getOperations().expire(key, times, TimeUnit.SECONDS);
        }
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

    /**
     * 是否能把键值放到换缓存中
     */
    public boolean setIfAbsent(final String key){
        return redisTemplate.opsForValue().setIfAbsent(key,key);
    }

    /**
     * 读取缓存
     */
    public Object get(final String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 写入缓存设置时效时间(指定redis数据源)
     */
    public boolean set(final String key, Object value, Long expireTime) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            if(!ObjectUtils.isEmpty(expireTime)){
                redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
