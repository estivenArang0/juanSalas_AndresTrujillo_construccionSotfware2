package com.banco.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name = "natural_person_clients")
public class NaturalPersonJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name", nullable = false) private String fullName;
    @Column(name = "identification_number", unique = true, nullable = false) private String identificationNumber;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String phone;
    @Column(name = "birth_date") private LocalDate birthDate;
    @Column(nullable = false) private String address;

    public NaturalPersonJpaEntity() {}

    public NaturalPersonJpaEntity(Long id, String fullName, String identificationNumber, String email, String phone, LocalDate birthDate, String address) {
        this.id = id;
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
    }

    public static NaturalPersonJpaEntityBuilder builder() { return new NaturalPersonJpaEntityBuilder(); }

    public static class NaturalPersonJpaEntityBuilder {
        private NaturalPersonJpaEntity e = new NaturalPersonJpaEntity();
        public NaturalPersonJpaEntityBuilder id(Long id) { e.id = id; return this; }
        public NaturalPersonJpaEntityBuilder fullName(String name) { e.fullName = name; return this; }
        public NaturalPersonJpaEntityBuilder identificationNumber(String id) { e.identificationNumber = id; return this; }
        public NaturalPersonJpaEntityBuilder email(String email) { e.email = email; return this; }
        public NaturalPersonJpaEntityBuilder phone(String phone) { e.phone = phone; return this; }
        public NaturalPersonJpaEntityBuilder birthDate(LocalDate date) { e.birthDate = date; return this; }
        public NaturalPersonJpaEntityBuilder address(String addr) { e.address = addr; return this; }
        public NaturalPersonJpaEntity build() { return e; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
}
