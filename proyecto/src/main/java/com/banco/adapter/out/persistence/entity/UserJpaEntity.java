package com.bank.app.adapter.out.persistence.entity;

import com.bank.app.domain.model.valueobject.UserRole;
import com.bank.app.domain.model.valueobject.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "users")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class UserJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "related_entity_id") private String relatedEntityId;
    @Column(name = "full_name", nullable = false) private String fullName;
    @Column(name = "identification_number", unique = true, nullable = false) private String identificationNumber;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String phone;
    @Column(name = "birth_date") private LocalDate birthDate;
    @Column(nullable = false) private String address;
    @Enumerated(EnumType.STRING) @Column(name = "role", nullable = false) private UserRole role;
    @Enumerated(EnumType.STRING) @Column(name = "status", nullable = false) private UserStatus status;
    @Column(unique = true, nullable = false) private String username;
    @Column(name = "password_hash", nullable = false) private String passwordHash;
}
