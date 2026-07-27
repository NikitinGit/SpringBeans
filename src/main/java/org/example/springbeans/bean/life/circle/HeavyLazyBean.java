package org.example.springbeans.bean.life.circle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("heavyLazyBean")
@Lazy
public// Спринг проигнорирует этот бин при старте!
class HeavyLazyBean implements BeanNameAware, InitializingBean, DisposableBean, SmartInitializingSingleton {
    private static final Logger log = LoggerFactory.getLogger(HeavyLazyBean.class);

    public HeavyLazyBean() {
        log.info("[3. HeavyLazyBean] -> 🛠️ ВЫЗОВ КОНСТРУКТОРА (Родился только сейчас!)");
    }

    @Override
    public void setBeanName(String name) {
        log.info("[3. HeavyLazyBean] -> 🏷️ BeanNameAware.setBeanName('{}')", name);
    }

    @PostConstruct
    public void init() {
        log.info("[3. HeavyLazyBean] -> 🚀 @PostConstruct (Тяжелые ресурсы загружены)");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("[3. HeavyLazyBean] -> ✅ InitializingBean.afterPropertiesSet");
    }

    // не срабатывает потому что @Lazy
    @Override
    public void afterSingletonsInstantiated() {
        log.info("[3. HeavyLazyBean] -> afterSingletonsInstantiated");
    }

    @PreDestroy
    public void preDestroyHook() {
        log.info("[3. HeavyLazyBean] -> ❌ @PreDestroy (Освобождаем гигабайты кэша)");
    }

    @Override
    public void destroy() {
        log.info("[3. HeavyLazyBean] -> 🔻 DisposableBean.destroy");
    }

    public void processHeavyMath() {
        log.info("[3. HeavyLazyBean] -> 🧮 Считаю сложные формулы...");
    }
}
