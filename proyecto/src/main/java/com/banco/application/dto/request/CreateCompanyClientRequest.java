package com.banco.application.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class CreateCompanyClientRequest {
    @NotBlank private String legalName;
    @NotBlank private String taxId;
    @NotBlank @Email private String email;
    @NotBlank @Size(min=7, max=15) private String phone;
    @NotBlank private String address;
    @NotBlank private String legalRepresentativeId;
    @NotBlank private String username;
    @NotBlank @Size(min=6) private String password;
}
