package org.example.springbeans;

import org.example.springbeans.bean.life.circle.BusinessService;
import org.example.springbeans.bean.life.circle.HeavyLazyBean;
import org.example.springbeans.bean.repo.DemoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;

@SpringBootApplication
@EnableAsync
public class SpringBeansApplication {

    private static final Logger log = LoggerFactory.getLogger(SpringBeansApplication.class);

    public static void main(String[] args) throws InterruptedException {
        //SpringApplication.run(SpringBeansApplication.class, args);
        // 1. Запускаем приложение и получаем управление над контекстом
        ConfigurableApplicationContext context = SpringApplication.run(SpringBeansApplication.class, args);
        log.info("=== 🟢 SPRING КОНТЕКСТ УСПЕШНО ЗАПУЩЕН ===");

        // 2. Достаем бизнес-сервис и вызываем его метод
        BusinessService service = context.getBean(BusinessService.class);
        service.doWork();
        // 1. Проверяем, прокси ли это вообще
        log.info("Это прокси? -> {}", AopUtils.isAopProxy(service));

        // 2. Проверяем, является ли он JDK Dynamic Proxy
        log.info("Это JDK Proxy? -> {}", AopUtils.isJdkDynamicProxy(service));

        // 3. Проверяем, является ли он CGLIB Proxy
        log.info("Это CGLIB Proxy? -> {}", AopUtils.isCglibProxy(service));

        // 4. Узнаем имя реального класса под оберткой
        log.info("Настоящий класс под прокси: {}", AopUtils.getTargetClass(service).getName());

        // 2.5 Проверяем DemoRepository — proxy-only бин без реального класса-реализации
        DemoRepository repository = context.getBean(DemoRepository.class);
        log.info("=== 🔍 DemoRepository ===");
        log.info("Реальный класс: {}", repository.getClass());
        log.info("Это JDK Dynamic Proxy? -> {}", AopUtils.isJdkDynamicProxy(repository));
        log.info("Интерфейсы прокси: {}", Arrays.toString(repository.getClass().getInterfaces()));
        // Реальный вызов метода — сработает наш доп. слой прокси из LifecycleLoggingBeanPostProcessor
        long count = repository.count();
        log.info("repository.count() = {}", count);

        log.info("=== 🤔 Обратите внимание: HeavyLazyBean еще НЕ СОЗДАН в логах выше! ===");
        Thread.sleep(1000); // пауза для наглядности в логах

        // 3. Впервые обращаемся к Lazy-бину. Именно в этот миг он родится!
        log.info("=== 💤 Запрашиваем HeavyLazyBean из контекста... ===");
        HeavyLazyBean lazyBean = context.getBean(HeavyLazyBean.class);
        lazyBean.processHeavyMath();

        Thread.sleep(1000);

        // 4. Закрываем контекст. Это запустит этап уничтожения (Destruction)
        log.info("=== 🛑 ЗАКРЫВАЕМ КОНТЕКСТ ПРИЛОЖЕНИЯ ===");
        context.close();
        log.info("=== 🎯 КОНТЕКСТ ЗАКРЫТ ===");
    }
}
