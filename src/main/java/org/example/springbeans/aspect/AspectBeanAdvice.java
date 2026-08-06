package org.example.springbeans.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectBeanAdvice {

    @Before("@annotation(testAspect)")
    public void checkAspect(JoinPoint joinpoint, AspectBean testAspect) {
        Class<?> testClass = joinpoint.getSignature().getDeclaringType();
        if (testClass != null) {
            System.out.println("testClass: " + testClass + ", testAspect.getName(); " + testAspect.getName());
        }
    }
}
