package com.bank.app.application.dto.response;
import lombok.Builder;
import lombok.Data;
@Data @Builder
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private Long userId;
}
