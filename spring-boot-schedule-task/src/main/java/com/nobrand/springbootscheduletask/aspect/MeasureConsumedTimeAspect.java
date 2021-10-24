package com.nobrand.springbootscheduletask.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


public class MeasureConsumedTimeAspect {

    public static String[] getTags(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MeasureConsumedTime annotation = method.getAnnotation(MeasureConsumedTime.class);
        return annotation.tags();
    }

}
