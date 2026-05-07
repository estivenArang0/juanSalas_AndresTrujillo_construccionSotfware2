package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.BankAccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BankAccountJpaRepository extends JpaRepository<BankAccountJpaEntity, Long> {
    Optional<BankAccountJpaEntity> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    List<BankAccountJpaEntity> findByOwnerId(String ownerId);
}
