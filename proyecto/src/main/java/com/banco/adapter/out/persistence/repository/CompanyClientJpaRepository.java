package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.CompanyClientJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyClientJpaRepository extends JpaRepository<CompanyClientJpaEntity, Long> {
    Optional<CompanyClientJpaEntity> findByTaxId(String taxId);
    boolean existsByTaxId(String taxId);
}
