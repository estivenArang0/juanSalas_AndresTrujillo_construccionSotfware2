package com.bank.app.domain.model.entity;

import com.bank.app.domain.model.valueobject.ClientStatus;
import com.bank.app.domain.model.valueobject.SystemRole;
import lombok.*;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class CompanyClient {
    private Long id;
    private String businessName;
    private String taxId;
    private String email;
    private String phoneNumber;
    private String address;
    private String legalRepresentativeId;

    // ✅ FIX 1: campos de estado y rol requeridos por el documento
    private ClientStatus status;
    private final SystemRole role = SystemRole.CLIENT_COMPANY;

    // ✅ FIX 2: validaciones de formato según reglas de negocio
    public static CompanyClient create(String businessName, String taxId, String email,
                                       String phoneNumber, String address,
                                       String legalRepresentativeId) {
        validateEmail(email);
        validatePhone(phoneNumber);
        validateRequired(businessName, "Business name");
        validateRequired(taxId, "Tax ID");
        validateRequired(address, "Address");
        validateRequired(legalRepresentativeId, "Legal representative ID");

        return CompanyClient.builder()
                .businessName(businessName)
                .taxId(taxId)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .legalRepresentativeId(legalRepresentativeId)
                .status(ClientStatus.ACTIVE)
                .build();
    }

    private static void validateEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains("."))
            throw new IllegalArgumentException("Invalid email format: " + email);
    }

    private static void validatePhone(String phone) {
        if (phone == null || phone.length() < 7 || phone.length() > 15)
            throw new IllegalArgumentException("Phone must be between 7 and 15 digits");
    }

    private static void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException(fieldName + " is required");
    }

    // ✅ FIX 3: solo métodos controlados modifican el estado
    public boolean isActive() { return ClientStatus.ACTIVE.equals(this.status); }
    public void block()       { this.status = ClientStatus.BLOCKED; }
    public void deactivate()  { this.status = ClientStatus.INACTIVE; }
    public void activate()    { this.status = ClientStatus.ACTIVE; }
}