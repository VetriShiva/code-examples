package com.vetri.poc.aop.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
    https://howtodoinjava.com/spring-boot2/aop-aspectj/
 */
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
    public void logAfterThrowing(JoinPoint joinPoint,Exception ex) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
//        AtomicInteger counter = new AtomicInteger();
//        String methodParams = String.join(",",Stream.of(paramNames).map(p->p+"="+paramValues[counter.getAndIncrement()]).collect(Collectors.toList()));

        String methodParams = String.join(",", IntStream.range(0, paramNames.length).mapToObj(i -> paramNames[i] + "=" + paramValues[i]).collect(Collectors.toList()));

        // Log the exception message
        StackTraceElement[] elements = ex.getStackTrace();
        log.error("className: {}, methodName: {}, methodParams: {}, Error: {}", className,methodName,methodParams,elements[0]);
    }

    @Around("execution(* com.vetri.poc.aop..*(..)))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();

        //Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        //Log method execution time
        log.info("Execution time of " + className + "." + methodName + " "
                + ":: " + stopWatch.getTotalTimeMillis() + " ms");

        return result;
    }
}
