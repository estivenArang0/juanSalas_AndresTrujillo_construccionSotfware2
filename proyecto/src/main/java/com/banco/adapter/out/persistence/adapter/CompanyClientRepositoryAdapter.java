package com.bank.app.adapter.out.persistence.adapter;

import com.bank.app.adapter.out.persistence.mapper.CompanyClientMapper;
import com.bank.app.adapter.out.persistence.repository.CompanyClientJpaRepository;
import com.bank.app.domain.model.entity.CompanyClient;
import com.bank.app.domain.repository.CompanyClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyClientRepositoryAdapter implements CompanyClientRepository {
    private final CompanyClientJpaRepository jpaRepository;
    private final CompanyClientMapper mapper;

    @Override public CompanyClient save(CompanyClient c) { return mapper.toDomain(jpaRepository.save(mapper.toJpa(c))); }
    @Override public Optional<CompanyClient> findById(Long id) { return jpaRepository.findById(id).map(mapper::toDomain); }
    @Override public Optional<CompanyClient> findByTaxId(String taxId) { return jpaRepository.findByTaxId(taxId).map(mapper::toDomain); }
    @Override public boolean existsByTaxId(String taxId) { return jpaRepository.existsByTaxId(taxId); }
}
