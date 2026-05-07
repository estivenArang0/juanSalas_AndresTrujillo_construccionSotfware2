package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.LoanMapper;
import com.banco.adapter.out.persistence.repository.LoanJpaRepository;
import com.banco.domain.model.entity.Loan;
import com.banco.domain.repository.LoanRepository;
import com.banco.domain.model.valueobject.LoanStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoanRepositoryAdapter implements LoanRepository {
    private final LoanJpaRepository jpa;
    private final LoanMapper mapper;

    @Override
    public Loan save(Loan loan) {
        return mapper.toDomain(jpa.save(mapper.toEntity(loan)));
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Loan> findByClientId(String clientId) {
        return jpa.findByClientId(clientId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByStatus(LoanStatus status) {
        return jpa.findByStatus(status).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
