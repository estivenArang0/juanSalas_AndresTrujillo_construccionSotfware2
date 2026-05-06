package com.bank.app.adapter.in.web.controller;

import com.bank.app.application.dto.request.ApproveLoanRequest;
import com.bank.app.application.dto.request.CreateLoanRequest;
import com.bank.app.application.dto.request.DisburseLoanRequest;
import com.bank.app.application.dto.response.ApiResponse;
import com.bank.app.application.dto.response.LoanResponse;
import com.bank.app.application.port.input.LoanInputPort;
import com.bank.app.config.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanInputPort loanInputPort;
    private final JwtService jwtService;

    @PostMapping
    @PreAuthorize("hasAnyRole('NATURAL_PERSON_CLIENT','COMPANY_CLIENT','COMMERCIAL_EMPLOYEE')")
    public ResponseEntity<ApiResponse<LoanResponse>> requestLoan(@Valid @RequestBody CreateLoanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Loan request submitted", loanInputPort.requestLoan(request)));
    }

    @PutMapping("/{loanId}/approve")
    @PreAuthorize("hasRole('INTERNAL_ANALYST')")
    public ResponseEntity<ApiResponse<LoanResponse>> approveLoan(
            @PathVariable Long loanId,
            @Valid @RequestBody ApproveLoanRequest request,
            HttpServletRequest httpRequest) {
        Long analystId = extractUserId(httpRequest);
        return ResponseEntity.ok(ApiResponse.ok("Loan approved",
                loanInputPort.approveLoan(loanId, request, analystId)));
    }

    @PutMapping("/{loanId}/reject")
    @PreAuthorize("hasRole('INTERNAL_ANALYST')")
    public ResponseEntity<ApiResponse<LoanResponse>> rejectLoan(
            @PathVariable Long loanId, HttpServletRequest httpRequest) {
        Long analystId = extractUserId(httpRequest);
        return ResponseEntity.ok(ApiResponse.ok("Loan rejected",
                loanInputPort.rejectLoan(loanId, analystId)));
    }

    @PutMapping("/{loanId}/disburse")
    @PreAuthorize("hasRole('INTERNAL_ANALYST')")
    public ResponseEntity<ApiResponse<LoanResponse>> disburseLoan(
            @PathVariable Long loanId,
            @Valid @RequestBody DisburseLoanRequest request,
            HttpServletRequest httpRequest) {
        Long analystId = extractUserId(httpRequest);
        return ResponseEntity.ok(ApiResponse.ok("Loan disbursed",
                loanInputPort.disburseLoan(loanId, request, analystId)));
    }

    @GetMapping("/{loanId}")
    @PreAuthorize("hasAnyRole('INTERNAL_ANALYST','COMMERCIAL_EMPLOYEE','NATURAL_PERSON_CLIENT','COMPANY_CLIENT')")
    public ResponseEntity<ApiResponse<LoanResponse>> getLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(ApiResponse.ok(loanInputPort.getLoanById(loanId)));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('INTERNAL_ANALYST','COMMERCIAL_EMPLOYEE','NATURAL_PERSON_CLIENT','COMPANY_CLIENT')")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getLoansByClient(@PathVariable String clientId) {
        return ResponseEntity.ok(ApiResponse.ok(loanInputPort.getLoansByClient(clientId)));
    }

    private Long extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtService.extractUserId(token);
    }
}
