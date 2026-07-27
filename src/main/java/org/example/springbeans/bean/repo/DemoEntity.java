package org.example.springbeans.bean.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DemoEntity {
    @Id
    private Long id;
    private String name;

    protected DemoEntity() {
    }

    public DemoEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
