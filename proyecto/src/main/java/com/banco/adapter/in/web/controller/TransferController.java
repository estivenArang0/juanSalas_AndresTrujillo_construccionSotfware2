package com.banco.adapter.in.web.controller;

import com.banco.application.dto.request.CreateTransferRequest;
import com.banco.application.dto.response.ApiResponse;
import com.banco.application.dto.response.TransferResponse;
import com.banco.application.port.input.TransferInputPort;
import com.banco.config.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferInputPort transferInputPort;
    private final JwtService jwtService;

    @PostMapping
    @PreAuthorize("hasAnyRole('NATURAL_PERSON_CLIENT','COMPANY_EMPLOYEE','COMPANY_SUPERVISOR')")
    public ResponseEntity<ApiResponse<TransferResponse>> createTransfer(
            @Valid @RequestBody CreateTransferRequest request,
            HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Transfer created",
                        transferInputPort.createTransfer(request, userId)));
    }

    @PutMapping("/{transferId}/approve")
    @PreAuthorize("hasRole('COMPANY_SUPERVISOR')")
    public ResponseEntity<ApiResponse<TransferResponse>> approveTransfer(
            @PathVariable Long transferId, HttpServletRequest httpRequest) {
        Long supervisorId = extractUserId(httpRequest);
        return ResponseEntity.ok(ApiResponse.ok("Transfer approved",
                transferInputPort.approveTransfer(transferId, supervisorId)));
    }

    @PutMapping("/{transferId}/reject")
    @PreAuthorize("hasRole('COMPANY_SUPERVISOR')")
    public ResponseEntity<ApiResponse<TransferResponse>> rejectTransfer(
            @PathVariable Long transferId, HttpServletRequest httpRequest) {
        Long supervisorId = extractUserId(httpRequest);
        return ResponseEntity.ok(ApiResponse.ok("Transfer rejected",
                transferInputPort.rejectTransfer(transferId, supervisorId)));
    }

    @GetMapping("/{transferId}")
    @PreAuthorize("hasAnyRole('COMPANY_SUPERVISOR','COMPANY_EMPLOYEE','INTERNAL_ANALYST','NATURAL_PERSON_CLIENT')")
    public ResponseEntity<ApiResponse<TransferResponse>> getTransfer(@PathVariable Long transferId) {
        return ResponseEntity.ok(ApiResponse.ok(transferInputPort.getTransferById(transferId)));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('COMPANY_SUPERVISOR','INTERNAL_ANALYST')")
    public ResponseEntity<ApiResponse<List<TransferResponse>>> getPending() {
        return ResponseEntity.ok(ApiResponse.ok(transferInputPort.getPendingTransfers()));
    }

    private Long extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtService.extractUserId(token);
    }
}
