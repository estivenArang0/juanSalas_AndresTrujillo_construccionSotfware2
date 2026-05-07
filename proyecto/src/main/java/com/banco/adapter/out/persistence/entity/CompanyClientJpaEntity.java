package com.banco.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity @Table(name = "company_clients")
public class CompanyClientJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "business_name", nullable = false) private String businessName;
    @Column(name = "tax_id", unique = true, nullable = false) private String taxId;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String phone;
    @Column(nullable = false) private String address;

    public CompanyClientJpaEntity() {}

    public CompanyClientJpaEntity(Long id, String businessName, String taxId, String email, String phone, String address) {
        this.id = id;
        this.businessName = businessName;
        this.taxId = taxId;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public static CompanyClientJpaEntityBuilder builder() { return new CompanyClientJpaEntityBuilder(); }

    public static class CompanyClientJpaEntityBuilder {
        private CompanyClientJpaEntity e = new CompanyClientJpaEntity();
        public CompanyClientJpaEntityBuilder id(Long id) { e.id = id; return this; }
        public CompanyClientJpaEntityBuilder businessName(String name) { e.businessName = name; return this; }
        public CompanyClientJpaEntityBuilder taxId(String id) { e.taxId = id; return this; }
        public CompanyClientJpaEntityBuilder email(String email) { e.email = email; return this; }
        public CompanyClientJpaEntityBuilder phone(String phone) { e.phone = phone; return this; }
        public CompanyClientJpaEntityBuilder address(String addr) { e.address = addr; return this; }
        public CompanyClientJpaEntity build() { return e; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
