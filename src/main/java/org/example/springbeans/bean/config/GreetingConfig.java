package org.example.springbeans.bean.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Никакого XML — обычный Java @Configuration класс.
// Бин "greetingFromBeanMethod" получит factoryBeanName="greetingConfig" и factoryMethodName="greetingFromBeanMethod".
@Configuration
public class GreetingConfig {

    @Bean
    public Greeting greetingFromBeanMethod() {
        return new Greeting("Привет из @Bean-метода");
    }
}