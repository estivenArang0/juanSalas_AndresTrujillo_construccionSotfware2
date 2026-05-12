package com.banco.application.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private Long userId;
}
