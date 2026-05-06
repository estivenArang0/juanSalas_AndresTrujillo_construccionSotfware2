package com.bank.app.application.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
@Data @Builder
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
