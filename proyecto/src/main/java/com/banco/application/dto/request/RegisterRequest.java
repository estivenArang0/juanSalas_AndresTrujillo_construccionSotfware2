package com.banco.application.dto.request;

import com.banco.domain.model.valueobject.UserRole;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Identification number is required")
    private String identificationNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Role is required")
    private UserRole role;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String relatedEntityId;
}
