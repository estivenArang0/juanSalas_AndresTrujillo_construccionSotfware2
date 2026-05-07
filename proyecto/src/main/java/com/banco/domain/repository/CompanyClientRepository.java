package com.banco.domain.repository;

import com.banco.domain.model.entity.CompanyClient;
import java.util.List;
import java.util.Optional;

public interface CompanyClientRepository {
    CompanyClient save(CompanyClient company);
    Optional<CompanyClient> findByTaxId(String taxId);
    List<CompanyClient> findAll();
    boolean existsByTaxId(String taxId);
}
