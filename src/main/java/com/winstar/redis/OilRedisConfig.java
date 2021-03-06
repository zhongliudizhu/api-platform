package com.winstar.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by zl on 2019/9/9
 */
@Configuration
@EnableCaching
public class OilRedisConfig extends RedisConfig {

    @Value("${spring.oil_redis.database}")
    private int dbIndex;

    @Value("${spring.oil_redis.host}")
    private String host;

    @Value("${spring.oil_redis.port}")
    private int port;

    @Value("${spring.oil_redis.password}")
    private String password;

    @Value("${spring.oil_redis.timeout}")
    private int timeout;

    /**
     * 配置redis连接工厂
     *
     * @return
     */
    @Primary
    @Bean
    public RedisConnectionFactory oilRedisConnectionFactory() {
        return createJedisConnectionFactory(dbIndex, host, port, password, timeout);
    }

    /**
     * 配置redisTemplate 注入方式使用@Resource(name="") 方式注入
     *
     * @return
     */
    @Bean(name = "oilRedisTemplate")
    public RedisTemplate cacheRedisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(oilRedisConnectionFactory());
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        template.setKeySerializer(redisSerializer);
        template.setHashKeySerializer(redisSerializer);
        return redisTemplate(template);
    }

}
