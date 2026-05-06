package com.bank.app.adapter.out.persistence.entity;

import com.bank.app.domain.model.valueobject.AccountStatus;
import com.bank.app.domain.model.valueobject.AccountType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "bank_accounts")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class BankAccountJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_number", unique = true, nullable = false) private String accountNumber;
    @Enumerated(EnumType.STRING) @Column(name = "account_type", nullable = false) private AccountType accountType;
    @Column(name = "owner_id", nullable = false) private String ownerId;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal balance;
    @Column(nullable = false, length = 3) private String currency;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private AccountStatus status;
    @Column(name = "opening_date") private LocalDate openingDate;
}
