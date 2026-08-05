package org.example.springbeans.bean.life.circle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("infrastructureBean")
//@Scope("prototype")
class InfrastructureBean implements BeanNameAware, InitializingBean, DisposableBean, SmartInitializingSingleton {
    private static final Logger log = LoggerFactory.getLogger(InfrastructureBean.class);
    public static int count;

    @Autowired
    private AfterPropertiesSet afterPropertiesSet;

    public InfrastructureBean() {
        // Собрать конфиг (HikariConfig: jdbcUrl/username/pool size) — без реального сетевого вызова, только объект-настройка
        log.info("[1. Infrastructure] -> 🛠️ ВЫЗОВ КОНСТРУКТОРА count; {}", ++count);
    }

    @Override
    public void setBeanName(String name) {
        // Присвоить имя пулу для JMX/метрик (HikariConfig.setPoolName(name)) — чтобы в мониторинге видеть, какой это бин
        log.info("[1. Infrastructure] -> 🏷️ BeanNameAware.setBeanName('{}')", name);
    }

    @PostConstruct
    public void init() {
        // Реально открыть соединения: dataSource.getConnection() + "SELECT 1", прогреть min-idle пул
        log.info("[1. Infrastructure] -> 🚀 @PostConstruct ;{}", afterPropertiesSet);
        afterPropertiesSet.test();
    }

    @Override
    public void afterPropertiesSet() {
        // Финальная проверка перед тем как отдать бин наружу: если пул не прогрелся — бросить исключение и остановить старт приложения
        log.info("[1. Infrastructure] -> ✅ InitializingBean.afterPropertiesSet();{}", afterPropertiesSet);
    }

    public void doWork(int id) {
        // Взять соединение из пула, выполнить запрос, вернуть соединение обратно в пул
        log.info("[1. Infrastructure] -> InfrastructureBean.doWork(int id) Business id:{}", id);
    }

    @PreDestroy
    public void preDestroyHook() {
        // Graceful shutdown: перестать принимать новые запросы, дождаться завершения активных
        log.info("[1. Infrastructure] -> ❌ @PreDestroy (Закрываем соединения, чистим память)");
    }

    @Override
    public void destroy() {
        // Принудительно закрыть пул целиком (dataSource.close()) — освободить сокеты/файловые дескрипторы
        log.info("[1. Infrastructure] -> 🔻 DisposableBean.destroy");
    }

    @Override
    public void afterSingletonsInstantiated() {
        // Все бины точно созданы (например MetricsRegistry) — только теперь безопасно зарегистрировать в нём метрики пула
        log.info("[1. Infrastructure] -> afterSingletonsInstantiated");
    }
}

