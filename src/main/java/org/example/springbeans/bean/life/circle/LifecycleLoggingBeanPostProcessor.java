package org.example.springbeans.bean.life.circle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return bean;
    }

    private boolean isInteresting(Object bean, String beanName) {
        return bean.getClass().getPackageName().equals("org.example.springbeans.bean.life.circle")
                || beanName.equals("demoRepository");
    }
}
