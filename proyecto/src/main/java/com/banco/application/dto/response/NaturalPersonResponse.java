package com.banco.application.dto.response;

import java.time.LocalDate;

public class NaturalPersonResponse {
    private Long id;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private String username;

    public NaturalPersonResponse() {}

    public NaturalPersonResponse(Long id, String fullName, String identificationNumber, String email, String phone, LocalDate birthDate, String address, String username) {
        this.id = id;
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.username = username;
    }

    public static NaturalPersonResponseBuilder builder() { return new NaturalPersonResponseBuilder(); }

    public static class NaturalPersonResponseBuilder {
        private NaturalPersonResponse r = new NaturalPersonResponse();
        public NaturalPersonResponseBuilder id(Long id) { r.id = id; return this; }
        public NaturalPersonResponseBuilder fullName(String name) { r.fullName = name; return this; }
        public NaturalPersonResponseBuilder identificationNumber(String id) { r.identificationNumber = id; return this; }
        public NaturalPersonResponseBuilder email(String email) { r.email = email; return this; }
        public NaturalPersonResponseBuilder phone(String phone) { r.phone = phone; return this; }
        public NaturalPersonResponseBuilder birthDate(LocalDate date) { r.birthDate = date; return this; }
        public NaturalPersonResponseBuilder address(String addr) { r.address = addr; return this; }
        public NaturalPersonResponseBuilder username(String uname) { r.username = uname; return this; }
        public NaturalPersonResponse build() { return r; }
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getIdentificationNumber() { return identificationNumber; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }
}
