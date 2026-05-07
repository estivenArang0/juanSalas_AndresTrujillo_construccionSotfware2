package com.banco.application.dto.response;

public class CompanyClientResponse {
    private Long id;
    private String legalName;
    private String taxId;
    private String email;
    private String phone;
    private String address;
    private String legalRepresentativeId;
    private String username;

    public CompanyClientResponse() {}

    public CompanyClientResponse(Long id, String legalName, String taxId, String email, String phone, String address, String legalRepresentativeId, String username) {
        this.id = id;
        this.legalName = legalName;
        this.taxId = taxId;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.legalRepresentativeId = legalRepresentativeId;
        this.username = username;
    }

    public static CompanyClientResponseBuilder builder() { return new CompanyClientResponseBuilder(); }

    public static class CompanyClientResponseBuilder {
        private CompanyClientResponse r = new CompanyClientResponse();
        public CompanyClientResponseBuilder id(Long id) { r.id = id; return this; }
        public CompanyClientResponseBuilder legalName(String name) { r.legalName = name; return this; }
        public CompanyClientResponseBuilder taxId(String id) { r.taxId = id; return this; }
        public CompanyClientResponseBuilder email(String email) { r.email = email; return this; }
        public CompanyClientResponseBuilder phone(String phone) { r.phone = phone; return this; }
        public CompanyClientResponseBuilder address(String addr) { r.address = addr; return this; }
        public CompanyClientResponseBuilder legalRepresentativeId(String id) { r.legalRepresentativeId = id; return this; }
        public CompanyClientResponseBuilder username(String uname) { r.username = uname; return this; }
        public CompanyClientResponse build() { return r; }
    }

    public Long getId() { return id; }
    public String getLegalName() { return legalName; }
    public String getTaxId() { return taxId; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getLegalRepresentativeId() { return legalRepresentativeId; }
    public String getUsername() { return username; }
}
