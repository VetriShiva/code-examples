package com.vetri.poc.aop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

@Slf4j
@Aspect
@Service
public class LogAspect {

    @Before("within(com.vetri.poc.aop..*)")
    public void logBefore(JoinPoint joinPoint) {
        // Log method name
        log.info("Invoke method: " + joinPoint.getSignature().toLongString());
        // Log arguments
        if(joinPoint.getArgs() != null) {
            for(Object obj : joinPoint.getArgs()) {
                log.info("Argument: " + obj.toString());
            }
        }
    }

    @AfterReturning(pointcut =  "within(com.vetri.poc.aop..*)", returning = "returnObject")
    public void logAfterReturning(JoinPoint joinPoint, Object returnObject) {
        // Log method name
        log.info("Return from method: " + joinPoint.getSignature().toLongString());
        // Log the return value
        log.info("Return Object: " + returnObject);
    }

    @AfterThrowing(pointcut = "within(com.vetri.poc.aop..*)", throwing = "ex")
    public void logAfterThrowing(Exception ex) {
        // Log the exception message
        log.info("Error: " + ex.getMessage());
    }
}
