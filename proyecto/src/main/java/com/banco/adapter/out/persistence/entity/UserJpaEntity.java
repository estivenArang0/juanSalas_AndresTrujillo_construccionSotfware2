package com.banco.adapter.out.persistence.entity;

import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.model.valueobject.UserStatus;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name = "users")
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

    public UserJpaEntity() {}

    public UserJpaEntity(Long id, String relatedEntityId, String fullName, String identificationNumber, String email, String phone, LocalDate birthDate, String address, UserRole role, UserStatus status, String username, String passwordHash) {
        this.id = id;
        this.relatedEntityId = relatedEntityId;
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.status = status;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public static UserJpaEntityBuilder builder() { return new UserJpaEntityBuilder(); }

    public static class UserJpaEntityBuilder {
        private UserJpaEntity e = new UserJpaEntity();
        public UserJpaEntityBuilder id(Long id) { e.id = id; return this; }
        public UserJpaEntityBuilder relatedEntityId(String id) { e.relatedEntityId = id; return this; }
        public UserJpaEntityBuilder fullName(String name) { e.fullName = name; return this; }
        public UserJpaEntityBuilder identificationNumber(String id) { e.identificationNumber = id; return this; }
        public UserJpaEntityBuilder email(String email) { e.email = email; return this; }
        public UserJpaEntityBuilder phone(String phone) { e.phone = phone; return this; }
        public UserJpaEntityBuilder birthDate(LocalDate date) { e.birthDate = date; return this; }
        public UserJpaEntityBuilder address(String addr) { e.address = addr; return this; }
        public UserJpaEntityBuilder role(UserRole role) { e.role = role; return this; }
        public UserJpaEntityBuilder status(UserStatus status) { e.status = status; return this; }
        public UserJpaEntityBuilder username(String uname) { e.username = uname; return this; }
        public UserJpaEntityBuilder passwordHash(String hash) { e.passwordHash = hash; return this; }
        public UserJpaEntity build() { return e; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(String relatedEntityId) { this.relatedEntityId = relatedEntityId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getIdentificationNumber() { return identificationNumber; }
    public void setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
