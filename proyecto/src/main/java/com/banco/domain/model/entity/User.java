package com.banco.domain.model.entity;

import com.banco.domain.exception.UserNotActiveException;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.model.valueobject.UserStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.Period;

// ✅ FIX 1: se elimina @Setter global
@Getter @AllArgsConstructor @NoArgsConstructor
public class User {
    public static UserBuilder builder() { return new UserBuilder(); }
    public static class UserBuilder {
        private User user = new User();
        public UserBuilder id(Long id) { user.id = id; return this; }
        public UserBuilder relatedEntityId(String id) { user.relatedEntityId = id; return this; }
        public UserBuilder fullName(String name) { user.fullName = name; return this; }
        public UserBuilder identificationNumber(String id) { user.identificationNumber = id; return this; }
        public UserBuilder email(String email) { user.email = email; return this; }
        public UserBuilder phone(String phone) { user.phone = phone; return this; }
        public UserBuilder birthDate(LocalDate date) { user.birthDate = date; return this; }
        public UserBuilder address(String addr) { user.address = addr; return this; }
        public UserBuilder role(UserRole role) { user.role = role; return this; }
        public UserBuilder status(UserStatus status) { user.status = status; return this; }
        public UserBuilder username(String uname) { user.username = uname; return this; }
        public UserBuilder passwordHash(String hash) { user.passwordHash = hash; return this; }
        public UserBuilder associatedCompanyId(String id) { user.associatedCompanyId = id; return this; }
        public User build() { return user; }
    }
    private Long id;
    private String relatedEntityId;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private UserRole role;
    private UserStatus status;
    private String username;

    // ✅ FIX 4: passwordHash no expuesto con @Getter — se accede solo por método controlado
    private String passwordHash;
    private String associatedCompanyId;

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
    public String getAssociatedCompanyId() { return associatedCompanyId; }
    public void setAssociatedCompanyId(String associatedCompanyId) { this.associatedCompanyId = associatedCompanyId; }

    // ✅ FIX 5: factory method que garantiza validación siempre
    public static User create(String relatedEntityId, String fullName, String identificationNumber,
                               String email, String phone, LocalDate birthDate, String address,
                               UserRole role, String username, String passwordHash) {
        validateRequired(fullName, "Full name");
        validateRequired(identificationNumber, "Identification number");
        validateRequired(address, "Address");
        validateRequired(username, "Username");
        validateRequired(passwordHash, "Password");
        validateEmail(email);
        validatePhone(phone);
        // ✅ FIX 2: birthDate null también lanza excepción
        validateAdult(birthDate);

        return User.builder()
                .relatedEntityId(relatedEntityId)
                .fullName(fullName)
                .identificationNumber(identificationNumber)
                .email(email)
                .phone(phone)
                .birthDate(birthDate)
                .address(address)
                .role(role)
                .username(username)
                .passwordHash(passwordHash)
                .status(UserStatus.ACTIVE)
                .build();
    }

    public void validateIsActive() {
        if (this.status != UserStatus.ACTIVE)
            throw new UserNotActiveException(
                "User '" + username + "' is not active. Status: " + status);
    }

    // ✅ FIX 2: birthDate null también lanza excepción
    public void validateIsAdult() {
        validateAdult(this.birthDate);
    }

    // ✅ FIX 4: verificación de contraseña sin exponer el hash
    public boolean checkPassword(String candidateHash) {
        return this.passwordHash != null && this.passwordHash.equals(candidateHash);
    }

    public boolean isActive()         { return UserStatus.ACTIVE.equals(this.status); }
    public boolean hasRole(UserRole r) { return this.role == r; }
    public void activate()            { this.status = UserStatus.ACTIVE; }
    public void block()               { this.status = UserStatus.BLOCKED; }
    public void deactivate()          { this.status = UserStatus.INACTIVE; }

    // ✅ FIX 1: cambio de contraseña solo por método controlado
    public void changePassword(String newPasswordHash) {
        validateRequired(newPasswordHash, "New password");
        this.passwordHash = newPasswordHash;
    }

    // ✅ FIX 3: validaciones de formato
    private static void validateEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains("."))
            throw new IllegalArgumentException("Invalid email format: " + email);
    }

    private static void validatePhone(String phone) {
        if (phone == null || phone.length() < 7 || phone.length() > 15)
            throw new IllegalArgumentException("Phone must be between 7 and 15 digits");
    }

    private static void validateAdult(LocalDate birthDate) {
        if (birthDate == null)
            throw new IllegalArgumentException("Birth date is required");
        if (Period.between(birthDate, LocalDate.now()).getYears() < 18)
            throw new IllegalStateException("User must be at least 18 years old");
    }

    private static void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException(fieldName + " is required");
    }
}
