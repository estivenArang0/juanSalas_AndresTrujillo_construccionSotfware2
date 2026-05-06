package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByIdIdentificacion(String identificationId);
    boolean existsByIdIdentificacion(String identificationId);
    boolean existsByUsername(String username);
}
