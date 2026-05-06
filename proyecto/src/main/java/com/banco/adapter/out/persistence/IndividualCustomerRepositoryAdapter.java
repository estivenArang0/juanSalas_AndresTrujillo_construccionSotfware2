package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.IndividualCustomerMapper;
import com.banco.adapter.out.persistence.repository.IndividualCustomerJpaRepository;
import com.banco.domain.model.IndividualCustomer;
import com.banco.domain.repository.IndividualCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class IndividualCustomerRepositoryAdapter implements IndividualCustomerRepository {
    private final IndividualCustomerJpaRepository jpa;
    private final IndividualCustomerMapper mapper;

    @Override public IndividualCustomer guardar(IndividualCustomer c) { return mapper.toDomain(jpa.save(mapper.toEntity(c))); }
    @Override public Optional<IndividualCustomer> buscarPorId(String id) { return jpa.findById(id).map(mapper::toDomain); }
    @Override public List<IndividualCustomer> listarTodos() { return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public boolean existePorId(String id) { return jpa.existsById(id); }
}
