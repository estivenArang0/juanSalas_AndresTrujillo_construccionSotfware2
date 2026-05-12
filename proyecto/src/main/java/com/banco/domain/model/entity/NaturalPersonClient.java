package com.banco.domain.model.entity;

import com.banco.domain.model.valueobject.UserStatus;
import com.banco.domain.model.valueobject.UserRole;
import java.time.LocalDate;

public class NaturalPersonClient {
    private Long id;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private UserStatus status;
    private final UserRole role = UserRole.NATURAL_PERSON_CLIENT;

    public NaturalPersonClient() {}

    public NaturalPersonClient(Long id, String fullName, String identificationNumber, String email, String phone, LocalDate birthDate, String address, UserStatus status) {
        this.id = id;
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.status = status;
    }

    public static NaturalPersonClientBuilder builder() { return new NaturalPersonClientBuilder(); }

    public static class NaturalPersonClientBuilder {
        private NaturalPersonClient c = new NaturalPersonClient();
        public NaturalPersonClientBuilder id(Long id) { c.id = id; return this; }
        public NaturalPersonClientBuilder fullName(String name) { c.fullName = name; return this; }
        public NaturalPersonClientBuilder identificationNumber(String id) { c.identificationNumber = id; return this; }
        public NaturalPersonClientBuilder email(String email) { c.email = email; return this; }
        public NaturalPersonClientBuilder phone(String phone) { c.phone = phone; return this; }
        public NaturalPersonClientBuilder birthDate(LocalDate date) { c.birthDate = date; return this; }
        public NaturalPersonClientBuilder address(String addr) { c.address = addr; return this; }
        public NaturalPersonClientBuilder status(UserStatus status) { c.status = status; return this; }
        public NaturalPersonClient build() { return c; }
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getIdentificationNumber() { return identificationNumber; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getAddress() { return address; }
    public UserStatus getStatus() { return status; }
    public UserRole getRole() { return role; }
    
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setStatus(UserStatus status) { this.status = status; }
    
    public void deactivate() { this.status = UserStatus.INACTIVE; }

    public void validateRequiredFields() {
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Full name is required");
        if (identificationNumber == null || identificationNumber.isBlank()) throw new IllegalArgumentException("Identification number is required");
    }
}
