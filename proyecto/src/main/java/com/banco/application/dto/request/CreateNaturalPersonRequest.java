package com.banco.application.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
@Data
public class CreateNaturalPersonRequest {
    @NotBlank private String fullName;
    @NotBlank private String identificationNumber;
    @NotBlank @Email private String email;
    @NotBlank @Size(min=7, max=15) private String phone;
    @NotNull private LocalDate birthDate;
    @NotBlank private String address;
    @NotBlank private String username;
    @NotBlank @Size(min=6) private String password;
}
