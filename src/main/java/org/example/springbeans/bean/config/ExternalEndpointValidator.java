package org.example.springbeans.bean.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

// Fail-fast: проверяем ВСЕ бины с @ExternalEndpoint ещё во время старта контекста,
// а не когда на них по ошибке придёт первый реальный запрос в проде.
@Component
public class ExternalEndpointValidator implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ExternalEndpoint annotation = bean.getClass().getAnnotation(ExternalEndpoint.class);
        if (annotation != null && annotation.url().isBlank()) {
            throw new IllegalStateException(
                    "Бин '%s' помечен @ExternalEndpoint, но url не задан".formatted(beanName));
        }
        return bean;
    }
}
