package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.BankAccountMapper;
import com.banco.adapter.out.persistence.repository.BankAccountJpaRepository;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BankAccountRepositoryAdapter implements BankAccountRepository {
    private final BankAccountJpaRepository jpa;
    private final BankAccountMapper mapper;

    @Override
    public BankAccount save(BankAccount account) {
        return mapper.toDomain(jpa.save(mapper.toEntity(account)));
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        return jpa.findByAccountNumber(accountNumber).map(mapper::toDomain);
    }

    @Override
    public List<BankAccount> findByOwnerId(String ownerId) {
        return jpa.findByOwnerId(ownerId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return jpa.existsByAccountNumber(accountNumber);
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        jpa.deleteByAccountNumber(accountNumber);
    }
}
