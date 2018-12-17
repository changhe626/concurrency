package com.onyx.concurrency.controller;

import com.onyx.concurrency.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cache")
public class CacheController {

    @Autowired
    private RedisClient redisClient;

    @GetMapping("get")
    public String get(@RequestParam("key")String key){
        return redisClient.get(key);
    }


    @GetMapping("set")
    public String set(@RequestParam("key")String key,@RequestParam("value")String value){
        redisClient.set(key,value);
        return "success";
    }


}
