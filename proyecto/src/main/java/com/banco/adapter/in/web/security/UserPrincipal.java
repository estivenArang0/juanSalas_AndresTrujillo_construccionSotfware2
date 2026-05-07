package com.banco.adapter.in.web.security;

public class UserPrincipal {
    private String username;
    private String role;
    private Long userId;

    public UserPrincipal() {}

    public UserPrincipal(String username, String role, Long userId) {
        this.username = username;
        this.role = role;
        this.userId = userId;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
