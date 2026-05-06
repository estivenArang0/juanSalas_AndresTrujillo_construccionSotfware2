package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.CorporateCustomerMapper;
import com.banco.adapter.out.persistence.repository.CorporateCustomerJpaRepository;
import com.banco.domain.model.CorporateCustomer;
import com.banco.domain.repository.CorporateCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CorporateCustomerRepositoryAdapter implements CorporateCustomerRepository {
    private final CorporateCustomerJpaRepository jpa;
    private final CorporateCustomerMapper mapper;

    @Override public CorporateCustomer guardar(CorporateCustomer e) { return mapper.toDomain(jpa.save(mapper.toEntity(e))); }
    @Override public Optional<CorporateCustomer> buscarPorNit(String taxId) { return jpa.findById(taxId).map(mapper::toDomain); }
    @Override public List<CorporateCustomer> listarTodos() { return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public boolean existePorNit(String taxId) { return jpa.existsById(taxId); }
}
