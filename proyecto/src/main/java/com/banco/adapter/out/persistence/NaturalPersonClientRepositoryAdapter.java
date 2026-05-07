package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.NaturalPersonClientMapper;
import com.banco.adapter.out.persistence.repository.NaturalPersonJpaRepository;
import com.banco.domain.model.entity.NaturalPersonClient;
import com.banco.domain.repository.NaturalPersonClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NaturalPersonClientRepositoryAdapter implements NaturalPersonClientRepository {
    private final NaturalPersonJpaRepository jpa;
    private final NaturalPersonClientMapper mapper;

    @Override
    public NaturalPersonClient save(NaturalPersonClient client) {
        return mapper.toDomain(jpa.save(mapper.toEntity(client)));
    }

    @Override
    public Optional<NaturalPersonClient> findByIdentificationNumber(String identificationNumber) {
        return jpa.findByIdentificationNumber(identificationNumber).map(mapper::toDomain);
    }

    @Override
    public List<NaturalPersonClient> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsByIdentificationNumber(String identificationNumber) {
        return jpa.existsByIdentificationNumber(identificationNumber);
    }
}
