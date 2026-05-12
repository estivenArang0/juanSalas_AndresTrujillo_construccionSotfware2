package com.banco.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCompanyClientRequest {
    private String legalName;
    @Email
    private String email;
    @Size(min = 7, max = 15)
    private String phone;
    private String address;
    private Long legalRepresentativeId;
}
