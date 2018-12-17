package com.onyx.concurrency.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

    @Value("${jedis.host}")
    private String ip;

    @Value("${jedis.port}")
    private int port;

    @Bean("jedisPool")
    public JedisPool jedisPool(){
        JedisPool pool = new JedisPool(ip, port);
        return pool;
    }


}
