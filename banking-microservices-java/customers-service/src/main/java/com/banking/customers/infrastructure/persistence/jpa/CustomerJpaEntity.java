package com.banking.customers.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class CustomerJpaEntity extends PersonJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer clientId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private boolean active = true;
}