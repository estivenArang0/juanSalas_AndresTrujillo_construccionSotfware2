package com.bank.app.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "company_clients")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class CompanyClientJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "legal_name", nullable = false) private String legalName;
    @Column(name = "tax_id", unique = true, nullable = false) private String taxId;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String phone;
    @Column(nullable = false) private String address;
    @Column(name = "legal_representative_id", nullable = false) private String legalRepresentativeId;
}
