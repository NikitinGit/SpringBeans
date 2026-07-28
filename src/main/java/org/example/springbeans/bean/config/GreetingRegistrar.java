package org.example.springbeans.bean.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

// Реальный (не-XML) способ явно задать значение конструктора в BeanDefinition —
// так регистрируют бины некоторые библиотеки/фреймворки (MyBatis mapper-scan, Feign-клиенты и т.п.).
@Component
public class GreetingRegistrar implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        BeanDefinition definition = BeanDefinitionBuilder
                .genericBeanDefinition(Greeting.class)
                .addConstructorArgValue("Привет из явного ConstructorArgumentValues")
                .getBeanDefinition();
        registry.registerBeanDefinition("greetingFromRegistrar", definition);
    }
}