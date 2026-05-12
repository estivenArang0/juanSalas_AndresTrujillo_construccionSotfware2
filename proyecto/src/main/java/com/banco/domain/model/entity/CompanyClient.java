package com.banco.domain.model.entity;

import com.banco.domain.model.valueobject.UserStatus;
import com.banco.domain.model.valueobject.UserRole;
import java.time.LocalDate;

public class CompanyClient {
    private Long id;
    private String businessName;
    private String taxId;
    private String email;
    private String phone;
    private LocalDate foundationDate;
    private String address;
    private String industry;
    private String legalRepresentativeId;
    private UserStatus status;
    private final UserRole role = UserRole.COMPANY_CLIENT;

    public CompanyClient() {}

    public CompanyClient(Long id, String businessName, String taxId, String email, String phone, LocalDate foundationDate, String address, String industry, String legalRepresentativeId, UserStatus status) {
        this.id = id;
        this.businessName = businessName;
        this.taxId = taxId;
        this.email = email;
        this.phone = phone;
        this.foundationDate = foundationDate;
        this.address = address;
        this.industry = industry;
        this.legalRepresentativeId = legalRepresentativeId;
        this.status = status;
    }

    public static CompanyClientBuilder builder() { return new CompanyClientBuilder(); }

    public static class CompanyClientBuilder {
        private CompanyClient c = new CompanyClient();
        public CompanyClientBuilder id(Long id) { c.id = id; return this; }
        public CompanyClientBuilder businessName(String name) { c.businessName = name; return this; }
        public CompanyClientBuilder legalName(String name) { c.businessName = name; return this; } // Alias for backward compat
        public CompanyClientBuilder taxId(String id) { c.taxId = id; return this; }
        public CompanyClientBuilder email(String email) { c.email = email; return this; }
        public CompanyClientBuilder phone(String phone) { c.phone = phone; return this; }
        public CompanyClientBuilder foundationDate(LocalDate date) { c.foundationDate = date; return this; }
        public CompanyClientBuilder address(String addr) { c.address = addr; return this; }
        public CompanyClientBuilder industry(String ind) { c.industry = ind; return this; }
        public CompanyClientBuilder legalRepresentativeId(String id) { c.legalRepresentativeId = id; return this; }
        public CompanyClientBuilder status(UserStatus status) { c.status = status; return this; }
        public CompanyClient build() { return c; }
    }

    public Long getId() { return id; }
    public String getBusinessName() { return businessName; }
    public String getLegalName() { return businessName; } // Alias
    public String getTaxId() { return taxId; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getFoundationDate() { return foundationDate; }
    public String getAddress() { return address; }
    public String getIndustry() { return industry; }
    public String getLegalRepresentativeId() { return legalRepresentativeId; }
    public UserStatus getStatus() { return status; }
    public UserRole getRole() { return role; }
    
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setLegalRepresentativeId(Long legalRepresentativeId) { this.legalRepresentativeId = String.valueOf(legalRepresentativeId); }
    
    public void deactivate() { this.status = UserStatus.INACTIVE; }

    public void validateRequiredFields() {
        if (businessName == null || businessName.isBlank()) throw new IllegalArgumentException("Business name is required");
        if (taxId == null || taxId.isBlank()) throw new IllegalArgumentException("Tax ID is required");
        if (legalRepresentativeId == null || legalRepresentativeId.isBlank()) throw new IllegalArgumentException("Legal representative ID is required");
    }
}
