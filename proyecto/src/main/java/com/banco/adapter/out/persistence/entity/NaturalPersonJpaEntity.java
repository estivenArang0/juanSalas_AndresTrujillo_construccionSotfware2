package com.bank.app.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "natural_person_clients")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class NaturalPersonJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name", nullable = false) private String fullName;
    @Column(name = "identification_number", unique = true, nullable = false) private String identificationNumber;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String phone;
    @Column(name = "birth_date") private LocalDate birthDate;
    @Column(nullable = false) private String address;
}
