package com.bank.app.adapter.out.persistence.adapter;

import com.bank.app.adapter.out.persistence.mapper.BankAccountMapper;
import com.bank.app.adapter.out.persistence.repository.BankAccountJpaRepository;
import com.bank.app.domain.model.entity.BankAccount;
import com.bank.app.domain.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BankAccountRepositoryAdapter implements BankAccountRepository {
    private final BankAccountJpaRepository jpaRepository;
    private final BankAccountMapper mapper;

    @Override public BankAccount save(BankAccount a) { return mapper.toDomain(jpaRepository.save(mapper.toJpa(a))); }
    @Override public Optional<BankAccount> findById(Long id) { return jpaRepository.findById(id).map(mapper::toDomain); }
    @Override public Optional<BankAccount> findByAccountNumber(String n) { return jpaRepository.findByAccountNumber(n).map(mapper::toDomain); }
    @Override public boolean existsByAccountNumber(String n) { return jpaRepository.existsByAccountNumber(n); }
    @Override public List<BankAccount> findByOwnerId(String id) { return jpaRepository.findByOwnerId(id).stream().map(mapper::toDomain).collect(Collectors.toList()); }
}
