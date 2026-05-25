package com.banking.accounts.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers_read_model")
@Getter
@Setter
@NoArgsConstructor
public class CustomerReadModelJpaEntity extends AuditableJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_id", unique = true, nullable = false)
    private Integer clientId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 50)
    private String identification;

    @Column(nullable = false)
    private boolean active = true;
}