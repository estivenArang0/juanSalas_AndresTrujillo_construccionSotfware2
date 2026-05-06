package com.bank.app.adapter.out.persistence.adapter;

import com.bank.app.adapter.out.persistence.mapper.NaturalPersonMapper;
import com.bank.app.adapter.out.persistence.repository.NaturalPersonJpaRepository;
import com.bank.app.domain.model.entity.NaturalPersonClient;
import com.bank.app.domain.repository.NaturalPersonClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NaturalPersonRepositoryAdapter implements NaturalPersonClientRepository {
    private final NaturalPersonJpaRepository jpaRepository;
    private final NaturalPersonMapper mapper;

    @Override public NaturalPersonClient save(NaturalPersonClient c) { return mapper.toDomain(jpaRepository.save(mapper.toJpa(c))); }
    @Override public Optional<NaturalPersonClient> findById(Long id) { return jpaRepository.findById(id).map(mapper::toDomain); }
    @Override public Optional<NaturalPersonClient> findByIdentificationNumber(String id) { return jpaRepository.findByIdentificationNumber(id).map(mapper::toDomain); }
    @Override public boolean existsByIdentificationNumber(String id) { return jpaRepository.existsByIdentificationNumber(id); }
}
