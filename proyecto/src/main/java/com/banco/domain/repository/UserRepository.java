package com.banco.domain.repository;

import com.banco.domain.model.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User usuario);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByIdentificationNumber(String identificationId);
    List<User> findAll();
    boolean existsByIdentificationNumber(String identificationId);
    boolean existsByUsername(String username);
}
