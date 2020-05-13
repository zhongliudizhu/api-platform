package com.winstar.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zl on 2019/9/9
 */
@Service
public class CouponRedisTools {

    @Resource(name = "couponRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 写入缓存设置时效时间(指定redis数据源)
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            if(!ObjectUtils.isEmpty(expireTime)){
                redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除对应的value
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 判断缓存中是否有对应的value
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     */
    public Object get(final String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 哈希获取数据
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 哈希删除数据
     */
    public Object hmRemove(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.delete(key, hashKey);
    }


    /**
     * 哈希删除数据
     */
    public void hmRemoveAll(String key, Object[] hashKeys) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        for (Object hashKey : hashKeys) {
            hash.delete(key, hashKey);
        }
    }

    /**
     * 哈希存数据（单个）
     */
    public void hmPut(String key, Object hashKey, Object value, Long time) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
        if(!ObjectUtils.isEmpty(time)){
            redisTemplate.expire(key, time, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 哈希存数据（集合）
     */
    public void hmPutAll(String key, Map<String, Object> map) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.putAll(key, map);
    }

    /**
     * 哈希获取数据
     */
    public Map<Object, Object> hmGetAll(String key){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.entries(key);
    }

    /**
     * 哈希是否存在
     */
    public boolean hmContains(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.hasKey(key, hashKey);
    }

    /**
     * 集合获取
     */
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 集合删除
     *
     * @param key
     * @return
     */
    public Long removeSetMembers(String key, Object... objects) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.remove(key, objects);
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
     * 集合是否存在
     */
    public boolean setExists(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.isMember(key, value);
    }

}
