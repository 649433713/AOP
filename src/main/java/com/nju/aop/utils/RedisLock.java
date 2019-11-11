package com.nju.aop.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author yinywf
 * Created on 2017/10/31
 */
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * function: 加锁
     * parameters:value 当前时间+超时时间
     * throw:
     * Created by yinywf
     */
    public boolean lock(String key, String value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }

        String currentValue = redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!currentValue.isEmpty() && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (!oldValue.isEmpty() && oldValue.equals(currentValue)) {
                return true;
            }
        }

        return false;
    }

    public void unlock(String key, String value) {

        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!currentValue.isEmpty() && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("【redistribute分布式锁】解锁异常,{}",e);
        }

    }



}
