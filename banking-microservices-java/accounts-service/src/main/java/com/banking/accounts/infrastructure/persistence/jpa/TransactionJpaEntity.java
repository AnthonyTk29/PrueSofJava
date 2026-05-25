package com.banking.accounts.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class TransactionJpaEntity extends AuditableJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;

    @Column(nullable = false)
    private Instant date;

    @Column(name = "transaction_type", nullable = false, length = 20)
    private String transactionType;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal balance;
}