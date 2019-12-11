package com.nju.aop.utils;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.lang.reflect.Field;

/**
 * @author yinywf
 * Created on 2019-12-11
 */
public class ExampleMatcherUtil {

    /**
     * function: 将一个对象转换成可供JPA模糊查询的实例（仅String类型参数模糊查询）
     * parameters: T object
     * throw:
     * Created by yinywf
     */
    public static <T> Example<T> transfer(T object) {
        ExampleMatcher matcher = ExampleMatcher.matching();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.getType() == String.class && field.get(object) != null) {
                    matcher = matcher.withMatcher(field.getName(), ExampleMatcher.GenericPropertyMatcher::contains);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 构造Example对象
        return Example.of(object, matcher);
    }
}
