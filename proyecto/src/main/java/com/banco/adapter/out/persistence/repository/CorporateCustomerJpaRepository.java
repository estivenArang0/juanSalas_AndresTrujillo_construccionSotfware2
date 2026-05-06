package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.CorporateCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorporateCustomerJpaRepository extends JpaRepository<CorporateCustomerEntity, String> {
    boolean existsById(String taxId);
}
