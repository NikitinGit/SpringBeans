package org.example.springbeans.bean.life.circle;

import org.aopalliance.intercept.MethodInterceptor;
import org.example.springbeans.bean.repo.DemoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class LifecycleLoggingBeanPostProcessor implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(LifecycleLoggingBeanPostProcessor.class);

    // Прокси JDK (java.lang.reflect.Proxy) живёт в СВОЁМ синтетическом пакете (jdk.proxy2...),
    // а не в пакете интерфейса — поэтому фильтруем по имени бина, а не по пакету класса.

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (isInteresting(bean, beanName)) {
            log.info("⚙️ BeanPostProcessor.postProcessBeforeInitialization('{}') -> {}", beanName, bean.getClass());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isInteresting(bean, beanName)) {
            log.info("⚙️ BeanPostProcessor.postProcessAfterInitialization('{}') -> {}", beanName, bean.getClass());
        }
        // "Родной" прокси Spring Data (demoRepository) уже полностью готов на этот момент.
        // Оборачиваем его ЕЩЁ ОДНИМ, своим слоем прокси — этот интерцептор сработает на КАЖДЫЙ вызов метода репозитория.
        // Проверяем именно тип (а не beanName!) — иначе заодно обернём саму JpaRepositoryFactoryBean
        // (она тоже зарегистрирована под именем 'demoRepository', но DemoRepository не реализует).
        if (bean instanceof DemoRepository) {
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            proxyFactory.addAdvice((MethodInterceptor) invocation -> {
                log.info("🔎 Вызов метода DemoRepository: {}()", invocation.getMethod().getName());
                return invocation.proceed();
            });
            return proxyFactory.getProxy();
        }
        return bean;
    }

    private boolean isInteresting(Object bean, String beanName) {
        return bean.getClass().getPackageName().equals("org.example.springbeans.bean.life.circle")
                || beanName.equals("demoRepository");
    }
}
