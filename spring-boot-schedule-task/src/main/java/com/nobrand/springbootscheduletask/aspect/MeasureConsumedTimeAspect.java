package com.nobrand.springbootscheduletask.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;


@Aspect
@Component
public class MeasureConsumedTimeAspect {

    private static final Logger log = LoggerFactory.getLogger(MeasureConsumedTimeAspect.class);

    public static String[] getTags(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MeasureConsumedTime annotation = method.getAnnotation(MeasureConsumedTime.class);
        return annotation.tags();
    }

    @Around(value = "@annotation(com.nobrand.springbootscheduletask.aspect.MeasureConsumedTime)")
    public Object doMeasureConsumedTime(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        StopWatch watch = new StopWatch();
        String msg = "consumed : %.3f(ms)";
        try {
            watch.start();
            result = pjp.proceed();
        } catch (Exception e) {
            msg = String.format("exception (%s) : ", e.getMessage()) + "%.3f(ms)";
        } finally {
            watch.stop();
            long consumed = watch.getTotalTimeNanos();
            log.info(String.format(msg, consumed/1000000.0));
        }
        return result;
    }

}
