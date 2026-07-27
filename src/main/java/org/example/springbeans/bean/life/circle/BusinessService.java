package org.example.springbeans.bean.life.circle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component("businessService")
@DependsOn("infrastructureBean")
public class BusinessService implements BeanNameAware, InitializingBean, DisposableBean { // Принудительно создаем ПОСЛЕ инфраструктуры
    private static final Logger log = LoggerFactory.getLogger(BusinessService.class);

    @Autowired
    private InfrastructureBean infrastructureBean;

    public BusinessService() {
        log.info("[2. BusinessService] -> 🛠️ ВЫЗОВ КОНСТРУКТОРА");
    }

    @Override
    public void setBeanName(String name) {
        log.info("[2. BusinessService] -> 🏷️ BeanNameAware.setBeanName('{}')", name);
    }

    @PostConstruct
    public void init() {
        log.info("[2. BusinessService] -> 🚀 @PostConstruct (Сервис готов к работе)");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("[2. BusinessService] -> ✅ InitializingBean.afterPropertiesSet");
    }

    @PreDestroy
    public void preDestroyHook() {
        log.info("[2. BusinessService] -> ❌ @PreDestroy (Завершаем бизнес-операции)");
    }

    @Override
    public void destroy() {
        log.info("[2. BusinessService] -> 🔻 DisposableBean.destroy");
    }

    @Async
    public void doWork() {
        log.info("[2. BusinessService] -> 💼 Выполняю важную работу...");
        infrastructureBean.doWork(25);
        log.info("infrastructureBean Это прокси? -> {}", AopUtils.isAopProxy(infrastructureBean));
    }
}
