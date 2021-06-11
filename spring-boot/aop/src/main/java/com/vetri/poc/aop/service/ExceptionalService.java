package com.vetri.poc.aop.service;

import org.springframework.stereotype.Service;

@Service
public class ExceptionalService {
    public void throwException(String code) throws Exception {
        throw new Exception("AOP Exception Test");
    }
}