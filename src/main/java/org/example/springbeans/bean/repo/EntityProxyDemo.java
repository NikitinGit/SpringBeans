package org.example.springbeans.bean.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EntityProxyDemo {
    private static final Logger log = LoggerFactory.getLogger(EntityProxyDemo.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void showHibernateProxy() {
        // getReference() НЕ идёт в базу — сразу возвращает lazy-прокси, реальный запрос будет при первом обращении к полю
        DemoEntity ref = entityManager.getReference(DemoEntity.class, 999L);
        log.info("=== 🧬 Hibernate proxy (не Spring!) ===");
        log.info("Класс через getReference(): {}", ref.getClass());
        log.info("Это java.lang.reflect.Proxy? -> {}", java.lang.reflect.Proxy.isProxyClass(ref.getClass()));
        log.info("Реальный класс — подкласс DemoEntity? -> {}", DemoEntity.class.isAssignableFrom(ref.getClass()));

        // Это НЕ список Spring-бинов, а отдельный реестр JPA-метаданных внутри EntityManagerFactory
        log.info("=== 📚 JPA Metamodel (реестр сущностей, не бинов!) ===");
        entityManager.getEntityManagerFactory().getMetamodel().getEntities()
                .forEach(e -> log.info(" - сущность: {} -> Java-класс: {}", e.getName(), e.getJavaType()));
    }
}