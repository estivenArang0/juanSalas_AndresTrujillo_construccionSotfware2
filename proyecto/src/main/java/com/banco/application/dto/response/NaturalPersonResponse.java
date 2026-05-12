package com.banco.application.dto.response;

import java.time.LocalDate;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NaturalPersonResponse {
    private Long id;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private String username;
}
