package com.banking.accounts.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class AccountJpaEntity extends AuditableJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_number", unique = true, nullable = false, length = 50)
    private String accountNumber;

    @Column(name = "account_type", nullable = false, length = 20)
    private String accountType;

    @Column(name = "initial_balance", precision = 18, scale = 2, nullable = false)
    private BigDecimal initialBalance;

    @Column(name = "available_balance", precision = 18, scale = 2, nullable = false)
    private BigDecimal availableBalance;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "customer_id", nullable = false)
    private Integer clientId;
}