package com.bank.app.adapter.out.persistence.adapter;

import com.bank.app.adapter.out.persistence.mapper.LoanMapper;
import com.bank.app.adapter.out.persistence.repository.LoanJpaRepository;
import com.bank.app.domain.model.entity.Loan;
import com.bank.app.domain.model.valueobject.LoanStatus;
import com.bank.app.domain.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoanRepositoryAdapter implements LoanRepository {
    private final LoanJpaRepository jpaRepository;
    private final LoanMapper mapper;

    @Override public Loan save(Loan l) { return mapper.toDomain(jpaRepository.save(mapper.toJpa(l))); }
    @Override public Optional<Loan> findById(Long id) { return jpaRepository.findById(id).map(mapper::toDomain); }
    @Override public List<Loan> findByClientId(String clientId) { return jpaRepository.findByClientId(clientId).stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public List<Loan> findByStatus(LoanStatus status) { return jpaRepository.findByStatus(status).stream().map(mapper::toDomain).collect(Collectors.toList()); }
}
