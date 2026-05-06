package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BankAccountJpaRepository extends JpaRepository<BankAccountEntity, String> {
    boolean existsById(String accountNumber);
    List<BankAccountEntity> findByIdTitular(String holderId);
}
