package com.banking.customers.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class PersonJpaEntity extends AuditableJpaEntity {

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 50)
    private String gender;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, unique = true, length = 50)
    private String identification;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;
}