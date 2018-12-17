package com.onyx.concurrency.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisClient {

    @Autowired
    @Qualifier("jedisPool")
    private JedisPool jedisPool;

    public void set(String key,String value) throws RuntimeException{
        Jedis jedis=null;
        try{
            jedis = jedisPool.getResource();
            jedis.set(key,value);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


    public String get(String key)throws RuntimeException{
        Jedis jedis=null;
        try{
            jedis = jedisPool.getResource();
            return jedis.get(key);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }




}
