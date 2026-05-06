package com.bank.app.adapter.in.web.controller;

import com.bank.app.application.dto.request.CreateBankAccountRequest;
import com.bank.app.application.dto.response.ApiResponse;
import com.bank.app.application.dto.response.BankAccountResponse;
import com.bank.app.application.port.input.BankAccountInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountInputPort bankAccountInputPort;

    @PostMapping
    @PreAuthorize("hasAnyRole('TELLER_EMPLOYEE','COMMERCIAL_EMPLOYEE','INTERNAL_ANALYST')")
    public ResponseEntity<ApiResponse<BankAccountResponse>> openAccount(
            @Valid @RequestBody CreateBankAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Account opened successfully",
                        bankAccountInputPort.openAccount(request)));
    }

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasAnyRole('TELLER_EMPLOYEE','COMMERCIAL_EMPLOYEE','INTERNAL_ANALYST','NATURAL_PERSON_CLIENT','COMPANY_CLIENT','COMPANY_EMPLOYEE','COMPANY_SUPERVISOR')")
    public ResponseEntity<ApiResponse<BankAccountResponse>> getByAccountNumber(
            @PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.ok(bankAccountInputPort.getAccountByNumber(accountNumber)));
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAnyRole('TELLER_EMPLOYEE','COMMERCIAL_EMPLOYEE','INTERNAL_ANALYST','NATURAL_PERSON_CLIENT','COMPANY_CLIENT','COMPANY_EMPLOYEE','COMPANY_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<BankAccountResponse>>> getByOwner(@PathVariable String ownerId) {
        return ResponseEntity.ok(ApiResponse.ok(bankAccountInputPort.getAccountsByOwner(ownerId)));
    }
}
