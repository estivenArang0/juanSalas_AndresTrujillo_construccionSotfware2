package com.banco.domain.repository;

import com.banco.domain.model.entity.BankAccount;
import java.util.List;
import java.util.Optional;

public interface BankAccountRepository {
    BankAccount save(BankAccount account);
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    List<BankAccount> findByOwnerId(String ownerId);
    boolean existsByAccountNumber(String accountNumber);
    void deleteByAccountNumber(String accountNumber);
}
