package com.sl.contact.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author 舒露
 */
@Component
public class BeanConfig {
    @Value("${redis.location}")
    private String redisLocation;

    @Bean
    public Jedis jedis() {
        return new Jedis(redisLocation);
    }
}
