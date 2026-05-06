package com.bank.app.application.dto.response;
import lombok.Builder;
import lombok.Data;
@Data @Builder
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
