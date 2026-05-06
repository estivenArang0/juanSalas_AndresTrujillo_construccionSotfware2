package com.banco.domain.repository;

import com.banco.domain.model.BankAccount;
import java.util.List;
import java.util.Optional;

public interface BankAccountRepository {
    BankAccount guardar(BankAccount account);
    Optional<BankAccount> buscarPorNumero(String accountNumber);
    List<BankAccount> buscarPorTitular(String holderId);
    boolean existePorNumero(String accountNumber);
}
