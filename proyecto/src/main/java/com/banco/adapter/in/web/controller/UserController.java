package com.bank.app.adapter.in.web.controller;

import com.bank.app.application.dto.request.CreateCompanyClientRequest;
import com.bank.app.application.dto.request.CreateNaturalPersonRequest;
import com.bank.app.application.dto.response.*;
import com.bank.app.application.port.input.UserInputPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserInputPort userInputPort;

    @PostMapping("/register/natural-person")
    public ResponseEntity<ApiResponse<NaturalPersonResponse>> registerNaturalPerson(
            @Valid @RequestBody CreateNaturalPersonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Natural person registered successfully",
                        userInputPort.registerNaturalPerson(request)));
    }

    @PostMapping("/register/company")
    public ResponseEntity<ApiResponse<CompanyClientResponse>> registerCompany(
            @Valid @RequestBody CreateCompanyClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Company registered successfully",
                        userInputPort.registerCompany(request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('INTERNAL_ANALYST','TELLER_EMPLOYEE','COMMERCIAL_EMPLOYEE')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userInputPort.getUserById(id)));
    }
}
