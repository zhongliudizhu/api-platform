package com.winstar.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by zl on 2019/9/9
 */
@Configuration
@EnableCaching
@SuppressWarnings("unchecked")
public class CouponRedisConfig extends RedisConfig {

    @Value("${spring.coupon_redis.database}")
    private int dbIndex;

    @Value("${spring.coupon_redis.host}")
    private String host;

    @Value("${spring.coupon_redis.port}")
    private int port;

    @Value("${spring.coupon_redis.password}")
    private String password;

    @Value("${spring.coupon_redis.timeout}")
    private int timeout;

    /**
     * 配置redis连接工厂
     *
     * @return RedisConnectionFactory
     */
    @Bean
    public RedisConnectionFactory couponRedisConnectionFactory() {
        return createJedisConnectionFactory(dbIndex, host, port, password, timeout);
    }

    /**
     * 配置redisTemplate 注入方式使用@Resource(name="") 方式注入
     *
     * @return RedisTemplate
     */
    @Bean(name = "couponRedisTemplate")
    public RedisTemplate cacheRedisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(couponRedisConnectionFactory());
        redisTemplate(template);
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(redisSerializer);
        template.setHashKeySerializer(redisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}
