package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByUsername(String username);
    Optional<UserJpaEntity> findByIdIdentificacion(String identificationId);
    boolean existsByIdIdentificacion(String identificationId);
    boolean existsByUsername(String username);
}
