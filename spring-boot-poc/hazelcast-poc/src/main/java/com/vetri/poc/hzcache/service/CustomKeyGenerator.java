package com.vetri.poc.hzcache.service;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Component
public class CustomKeyGenerator implements KeyGenerator {
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(target.getClass().getName())
                .append("_")
                .append(method.getName())
                .append("_")
                .append(StringUtils.arrayToDelimitedString(params, "_"));
        return keyBuilder.toString();
    }
}
