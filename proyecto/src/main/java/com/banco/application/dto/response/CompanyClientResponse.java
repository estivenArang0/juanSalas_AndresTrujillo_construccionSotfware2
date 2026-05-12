package com.banco.application.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CompanyClientResponse {
    private Long id;
    private String legalName;
    private String taxId;
    private String email;
    private String phone;
    private String address;
    private String legalRepresentativeId;
    private String username;
}
