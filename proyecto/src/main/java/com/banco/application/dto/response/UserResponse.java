package com.bank.app.application.dto.response;
import com.bank.app.domain.model.valueobject.UserRole;
import com.bank.app.domain.model.valueobject.UserStatus;
import lombok.Builder;
import lombok.Data;
@Data @Builder
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
