package com.bank.app.domain.model.entity;

import com.bank.app.domain.model.valueobject.ClientStatus;
import com.bank.app.domain.model.valueobject.SystemRole;
import lombok.*;
import java.time.LocalDate;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class NaturalPersonClient {
    private Long id;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;

    // ✅ FIX 3: campos requeridos por el documento para todos los clientes
    private ClientStatus status;
    private final SystemRole role = SystemRole.CLIENT_NATURAL_PERSON;

    // ✅ FIX 4: factory method que garantiza validación siempre
    public static NaturalPersonClient create(String fullName, String identificationNumber,
                                              String email, String phone,
                                              LocalDate birthDate, String address) {
        validateRequired(fullName, "Full name");
        validateRequired(identificationNumber, "Identification number");
        validateRequired(address, "Address");
        validateEmail(email);
        validatePhone(phone);
        // ✅ FIX 1: validación de mayoría de edad
        validateAdult(birthDate);

        return NaturalPersonClient.builder()
                .fullName(fullName)
                .identificationNumber(identificationNumber)
                .email(email)
                .phone(phone)
                .birthDate(birthDate)
                .address(address)
                .status(ClientStatus.ACTIVE)
                .build();
    }

    // ✅ FIX 2: validación de formato de email
    private static void validateEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains("."))
            throw new IllegalArgumentException("Invalid email format: " + email);
    }

    private static void validatePhone(String phone) {
        if (phone == null || phone.length() < 7 || phone.length() > 15)
            throw new IllegalArgumentException("Phone must be between 7 and 15 digits");
    }

    // ✅ FIX 1: debe tener al menos 18 años
    private static void validateAdult(LocalDate birthDate) {
        if (birthDate == null)
            throw new IllegalArgumentException("Birth date is required");
        if (birthDate.plusYears(18).isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Client must be at least 18 years old");
    }

    private static void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException(fieldName + " is required");
    }

    // ✅ FIX 5: solo métodos controlados modifican el estado
    public boolean isActive()  { return ClientStatus.ACTIVE.equals(this.status); }
    public void block()        { this.status = ClientStatus.BLOCKED; }
    public void deactivate()   { this.status = ClientStatus.INACTIVE; }
    public void activate()     { this.status = ClientStatus.ACTIVE; }
}