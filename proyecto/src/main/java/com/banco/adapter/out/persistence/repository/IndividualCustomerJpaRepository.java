package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.IndividualCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualCustomerJpaRepository extends JpaRepository<IndividualCustomerEntity, String> {
    boolean existsById(String id);
}
