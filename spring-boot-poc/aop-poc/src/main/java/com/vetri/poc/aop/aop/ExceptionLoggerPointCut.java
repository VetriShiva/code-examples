package com.vetri.poc.aop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Aspect
public class ExceptionLoggerPointCut {
    @AfterThrowing(pointcut = "execution(* com.vetri.poc.aop.*.*.*(..))", throwing = "ex")
    public void logError(JoinPoint joinPoint, Exception ex) {

        Object[] signatureArgs = joinPoint.getArgs();
        for (Object signatureArg: signatureArgs) {
            log.info("Arg: " + signatureArg);
        }
        log.info("After Throwing exception in method:"+joinPoint.getSignature());
        //log.info("Exception is:"+ex.getMessage());
        log.error("Exception : "+ex.getMessage());
        //ex.printStackTrace();
    }
}
