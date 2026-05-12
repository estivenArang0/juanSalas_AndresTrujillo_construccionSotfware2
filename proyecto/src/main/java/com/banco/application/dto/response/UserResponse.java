package com.banco.application.dto.response;

import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.model.valueobject.UserStatus;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String identificationNumber;
    private String email;
    private String phone;
    private String address;
    private UserRole role;
    private UserStatus status;
    private String username;
}
