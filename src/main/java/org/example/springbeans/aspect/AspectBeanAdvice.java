package org.example.springbeans.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
@Order(1) // меньше значение = выше приоритет = ближе к "снаружи" (сработает раньше @Transactional, который по умолчанию Ordered.LOWEST_PRECEDENCE)
public class AspectBeanAdvice {

    @Before("@annotation(testAspect)")
    public void checkAspect(JoinPoint joinpoint, AspectBean testAspect) {
        Class<?> testClass = joinpoint.getSignature().getDeclaringType();
        if (testClass != null) {
            System.out.println("AspectBeanAdvice, транзакция активна? -> " + TransactionSynchronizationManager.isActualTransactionActive());
        }
    }
}
