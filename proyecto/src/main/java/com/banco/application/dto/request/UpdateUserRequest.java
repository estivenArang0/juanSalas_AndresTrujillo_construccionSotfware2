package com.banco.application.dto.request;

import com.banco.domain.model.valueobject.UserRole;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;
    @Email
    private String email;
    private String phone;
    private String address;
    private UserRole role;
}
