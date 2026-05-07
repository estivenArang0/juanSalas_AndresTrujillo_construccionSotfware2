package com.banco.application.dto.response;

import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.model.valueobject.UserStatus;

public class UserResponse {
    private Long id;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private String address;
    private UserRole role;
    private UserStatus status;
    private String username;

    public UserResponse() {}

    public UserResponse(Long id, String fullName, String identificationNumber, String email, String phone, String address, UserRole role, UserStatus status, String username) {
        this.id = id;
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.status = status;
        this.username = username;
    }

    public static UserResponseBuilder builder() { return new UserResponseBuilder(); }

    public static class UserResponseBuilder {
        private UserResponse r = new UserResponse();
        public UserResponseBuilder id(Long id) { r.id = id; return this; }
        public UserResponseBuilder fullName(String name) { r.fullName = name; return this; }
        public UserResponseBuilder identificationNumber(String id) { r.identificationNumber = id; return this; }
        public UserResponseBuilder email(String email) { r.email = email; return this; }
        public UserResponseBuilder phone(String phone) { r.phone = phone; return this; }
        public UserResponseBuilder address(String addr) { r.address = addr; return this; }
        public UserResponseBuilder role(UserRole role) { r.role = role; return this; }
        public UserResponseBuilder status(UserStatus status) { r.status = status; return this; }
        public UserResponseBuilder username(String uname) { r.username = uname; return this; }
        public UserResponse build() { return r; }
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getIdentificationNumber() { return identificationNumber; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public UserRole getRole() { return role; }
    public UserStatus getStatus() { return status; }
    public String getUsername() { return username; }
}
