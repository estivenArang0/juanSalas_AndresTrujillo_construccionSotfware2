package com.banco.domain.repository;

import com.banco.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User guardar(User usuario);
    Optional<User> buscarPorId(Long id);
    Optional<User> buscarPorUsername(String username);
    Optional<User> buscarPorIdIdentificacion(String identificationId);
    List<User> listarTodos();
    boolean existePorIdIdentificacion(String identificationId);
    boolean existePorUsername(String username);
}
