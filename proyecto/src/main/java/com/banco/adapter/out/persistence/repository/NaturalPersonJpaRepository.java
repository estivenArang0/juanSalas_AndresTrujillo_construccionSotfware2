package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.NaturalPersonJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NaturalPersonJpaRepository extends JpaRepository<NaturalPersonJpaEntity, Long> {
    Optional<NaturalPersonJpaEntity> findByIdentificationNumber(String identificationNumber);
    boolean existsByIdentificationNumber(String identificationNumber);
}
