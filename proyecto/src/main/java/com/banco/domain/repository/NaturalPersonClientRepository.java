package com.banco.domain.repository;

import com.banco.domain.model.entity.NaturalPersonClient;
import java.util.List;
import java.util.Optional;

public interface NaturalPersonClientRepository {
    NaturalPersonClient save(NaturalPersonClient client);
    Optional<NaturalPersonClient> findByIdentificationNumber(String identificationNumber);
    List<NaturalPersonClient> findAll();
    boolean existsByIdentificationNumber(String identificationNumber);
}
