package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.BankAccountMapper;
import com.banco.adapter.out.persistence.repository.BankAccountJpaRepository;
import com.banco.domain.model.BankAccount;
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

    @Override public BankAccount guardar(BankAccount c) { return mapper.toDomain(jpa.save(mapper.toEntity(c))); }
    @Override public Optional<BankAccount> buscarPorNumero(String num) { return jpa.findById(num).map(mapper::toDomain); }
    @Override public List<BankAccount> buscarPorTitular(String id) { return jpa.findByIdTitular(id).stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public boolean existePorNumero(String num) { return jpa.existsById(num); }
}
