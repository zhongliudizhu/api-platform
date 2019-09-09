package com.winstar.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by zl on 2019/9/9
 */
@Configuration
@EnableCaching
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
     * @return
     */
    @Bean
    public RedisConnectionFactory couponRedisConnectionFactory() {
        return createJedisConnectionFactory(dbIndex, host, port, password, timeout);
    }

    /**
     * 配置redisTemplate 注入方式使用@Resource(name="") 方式注入
     *
     * @return
     */
    @Bean(name = "couponRedisTemplate")
    public RedisTemplate cacheRedisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(couponRedisConnectionFactory());
        redisTemplate(template);
        template.afterPropertiesSet();
        return template;
    }

}
