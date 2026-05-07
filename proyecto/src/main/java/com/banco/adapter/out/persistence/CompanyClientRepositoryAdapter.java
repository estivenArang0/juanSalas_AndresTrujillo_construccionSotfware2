package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.CompanyClientMapper;
import com.banco.adapter.out.persistence.repository.CompanyClientJpaRepository;
import com.banco.domain.model.entity.CompanyClient;
import com.banco.domain.repository.CompanyClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompanyClientRepositoryAdapter implements CompanyClientRepository {
    private final CompanyClientJpaRepository jpa;
    private final CompanyClientMapper mapper;

    @Override
    public CompanyClient save(CompanyClient company) {
        return mapper.toDomain(jpa.save(mapper.toEntity(company)));
    }

    @Override
    public Optional<CompanyClient> findByTaxId(String taxId) {
        return jpa.findByTaxId(taxId).map(mapper::toDomain);
    }

    @Override
    public List<CompanyClient> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsByTaxId(String taxId) {
        return jpa.existsByTaxId(taxId);
    }
}
