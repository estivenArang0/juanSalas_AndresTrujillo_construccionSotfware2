package com.bank.app.adapter.out.persistence.adapter;

import com.bank.app.adapter.out.persistence.mapper.UserMapper;
import com.bank.app.adapter.out.persistence.repository.UserJpaRepository;
import com.bank.app.domain.model.entity.User;
import com.bank.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Override public User save(User u) { return mapper.toDomain(jpaRepository.save(mapper.toJpa(u))); }
    @Override public Optional<User> findById(Long id) { return jpaRepository.findById(id).map(mapper::toDomain); }
    @Override public Optional<User> findByUsername(String username) { return jpaRepository.findByUsername(username).map(mapper::toDomain); }
    @Override public Optional<User> findByIdentificationNumber(String id) { return jpaRepository.findByIdentificationNumber(id).map(mapper::toDomain); }
    @Override public boolean existsByIdentificationNumber(String id) { return jpaRepository.existsByIdentificationNumber(id); }
    @Override public boolean existsByUsername(String username) { return jpaRepository.existsByUsername(username); }
    @Override public List<User> findAll() { return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList()); }
}
