package com.banco.application.dto.response;

public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private Long userId;

    public AuthResponse() {}

    public AuthResponse(String token, String username, String role, Long userId) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.userId = userId;
    }

    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }

    public static class AuthResponseBuilder {
        private AuthResponse r = new AuthResponse();
        public AuthResponseBuilder token(String t) { r.token = t; return this; }
        public AuthResponseBuilder username(String u) { r.username = u; return this; }
        public AuthResponseBuilder role(String role) { r.role = role; return this; }
        public AuthResponseBuilder userId(Long id) { r.userId = id; return this; }
        public AuthResponse build() { return r; }
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public Long getUserId() { return userId; }
}
