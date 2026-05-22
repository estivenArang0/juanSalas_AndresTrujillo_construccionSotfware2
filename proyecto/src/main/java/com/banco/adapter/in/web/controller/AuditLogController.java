package com.banco.adapter.in.web.controller;

import com.banco.domain.model.AuditLog;
import com.banco.domain.repository.AuditLogRepository;
import com.banco.application.dto.response.ApiResponse;
import com.banco.config.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;
    private final JwtService jwtService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('INTERNAL_ANALYST')")
    public ApiResponse<List<AuditLog>> getAll() {
        return ApiResponse.ok(auditLogRepository.listarTodos());
    }

    @GetMapping("/my-operations")
    @PreAuthorize("hasAnyRole('NATURAL_PERSON_CLIENT','COMPANY_EMPLOYEE','COMPANY_SUPERVISOR')")
    public ApiResponse<List<AuditLog>> getMyOperations(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtService.extractUserId(token);
        return ApiResponse.ok(auditLogRepository.buscarPorUsuario(userId));
    }
}