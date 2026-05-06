package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.UserMapper;
import com.banco.adapter.out.persistence.repository.UserJpaRepository;
import com.banco.domain.model.User;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository jpa;
    private final UserMapper mapper;

    @Override public User guardar(User u) { return mapper.toDomain(jpa.save(mapper.toEntity(u))); }
    @Override public Optional<User> buscarPorId(Long id) { return jpa.findById(id).map(mapper::toDomain); }
    @Override public Optional<User> buscarPorUsername(String username) { return jpa.findByUsername(username).map(mapper::toDomain); }
    @Override public Optional<User> buscarPorIdIdentificacion(String id) { return jpa.findByIdIdentificacion(id).map(mapper::toDomain); }
    @Override public List<User> listarTodos() { return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public boolean existePorIdIdentificacion(String id) { return jpa.existsByIdIdentificacion(id); }
    @Override public boolean existePorUsername(String u) { return jpa.existsByUsername(u); }
}
