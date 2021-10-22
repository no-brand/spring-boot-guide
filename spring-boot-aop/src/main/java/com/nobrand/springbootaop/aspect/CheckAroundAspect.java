package com.nobrand.springbootaop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class CheckAroundAspect {

    private static final Logger log = LoggerFactory.getLogger(CheckAroundAspect.class);

    public String[] getActions(JoinPoint joinPoint) {
        // Spring AOP 는 method 에 적용됩니다.
        // JoinPoint 를 통해서 Method (Reflection) 객체, 그 안의 annotation 에 접근합니다.
        // 단, annotation 을 읽어오는 방식이 reflection 이라면, 컴파일타임 에러체크가 불가능해서 비선호되지 않을까요?
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CheckAround annotation = method.getAnnotation(CheckAround.class);
        return annotation.actions();
    }

    @Before(value = "@annotation(com.nobrand.springbootaop.aspect.CheckAround)")
    public void checkBeforeAnnotation(JoinPoint joinPoint) {
        log.info("Before execution @Before (annotation signature): " + Arrays.toString(getActions(joinPoint)));
    }

    @Before(value = "execution(* com.nobrand.springbootaop.controller.BaseController.index(..))")
    public void checkBeforeMethod(JoinPoint joinPoint) {
        log.info("Before execution @Before (method signature): " + Arrays.toString(getActions(joinPoint)));
    }

    @AfterReturning(value = "@annotation(com.nobrand.springbootaop.aspect.CheckAround)")
    public void checkAfterReturning(JoinPoint joinPoint) {
        log.info("After execution @AfterReturning: " + Arrays.toString(getActions(joinPoint)));
    }

    @AfterThrowing(value = "@annotation(com.nobrand.springbootaop.aspect.CheckAround)", throwing = "ex")
    public void checkAfterReturning(JoinPoint joinPoint, Exception ex) {
        log.info("After execution @AfterThrowing: " + Arrays.toString(getActions(joinPoint)) + ", " + ex.getMessage());
    }

    @After(value = "@annotation(com.nobrand.springbootaop.aspect.CheckAround)")
    public void checkAfter(JoinPoint joinPoint) {
        log.info("After execution @After: " + Arrays.toString(getActions(joinPoint)));
    }

    @Around(value = "@annotation(com.nobrand.springbootaop.aspect.CheckAround)")
    public Object checkAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before execution @Around: " + Arrays.toString(getActions(joinPoint)));
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            log.info("After execution @Around: " + Arrays.toString(getActions(joinPoint)));
        }
        return result;
    }

}
