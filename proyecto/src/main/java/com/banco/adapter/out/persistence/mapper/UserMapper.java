package com.banco.adapter.out.persistence.mapper;

import com.banco.adapter.out.persistence.entity.UserJpaEntity;
import com.banco.domain.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserJpaEntity e) {
        if (e == null) return null;
        return User.builder()
                .id(e.getId())
                .relatedEntityId(e.getRelatedEntityId())
                .fullName(e.getFullName())
                .identificationNumber(e.getIdentificationNumber())
                .email(e.getEmail())
                .phone(e.getPhone())
                .birthDate(e.getBirthDate())
                .address(e.getAddress())
                .role(e.getRole())
                .status(e.getStatus())
                .username(e.getUsername())
                .passwordHash(e.getPasswordHash())
                .build();
    }

    public UserJpaEntity toEntity(User d) {
        if (d == null) return null;
        return UserJpaEntity.builder()
                .id(d.getId())
                .relatedEntityId(d.getRelatedEntityId())
                .fullName(d.getFullName())
                .identificationNumber(d.getIdentificationNumber())
                .email(d.getEmail())
                .phone(d.getPhone())
                .birthDate(d.getBirthDate())
                .address(d.getAddress())
                .role(d.getRole())
                .status(d.getStatus())
                .username(d.getUsername())
                .passwordHash(d.getPasswordHash())
                .build();
    }
}
