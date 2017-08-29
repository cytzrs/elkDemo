package com.elastic.stack.demo.elkDemo.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by liyang on 2017/8/24.
 */
@Aspect
@Component
public class IndexAspect {

    private final static Logger logger = LoggerFactory.getLogger(IndexAspect.class);

    @Around("@annotation(com.elastic.stack.demo.elkDemo.annotation.IndexAnno))")
    public void doProxy(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature sig = proceedingJoinPoint.getSignature();
        if (sig instanceof MethodSignature) {
            final Method method = ((MethodSignature) sig).getMethod();
            IndexAnno indexAnno = method.getAnnotation(IndexAnno.class);
            if (indexAnno == null) {
                return;
            } else {
                proceedingJoinPoint.proceed();
            }
            logger.info("----------------index proxy generate...-------------------");
        }
    }
}
