package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.response.CompanyClientResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.CompanyClient;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.CorporateCustomerRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyClientUpdateService {

    private final CorporateCustomerRepository corporateCustomerRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public CompanyClientResponse update(String taxId, String newEmail,
                                        String newPhone, String newAddress,
                                        Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        // Solo empleados comerciales o analistas pueden actualizar clientes empresa
        if (!requestingUser.hasRole(UserRole.COMMERCIAL_EMPLOYEE) &&
            !requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo empleados comerciales o analistas pueden actualizar clientes empresa");
        }

        if (!companyClientExists(taxId)) {
            throw new ResourceNotFoundException(
                    "No existe una empresa registrada con el NIT: " + taxId);
        }

        CompanyClient company = corporateCustomerRepository.findByTaxId(taxId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una empresa registrada con el NIT: " + taxId));

        // Solo se pueden actualizar campos de contacto — el NIT y razón social son inmutables
        CompanyClient updated = CompanyClient.builder()
                .id(company.getId())
                .businessName(company.getBusinessName())
                .taxId(company.getTaxId())
                .email(newEmail != null && !newEmail.isBlank() ? newEmail : company.getEmail())
                .phoneNumber(newPhone != null && !newPhone.isBlank() ? newPhone : company.getPhoneNumber())
                .address(newAddress != null && !newAddress.isBlank() ? newAddress : company.getAddress())
                .legalRepresentativeId(company.getLegalRepresentativeId())
                .status(company.getStatus())
                .build();

        CompanyClient saved = corporateCustomerRepository.save(updated);

        auditLog.log(AuditLogRequest.builder()
                .operationType("COMPANY_CLIENT_UPDATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(taxId)
                .details(Map.of(
                        "taxId", taxId,
                        "updatedFields", "email, phone, address",
                        "updatedBy", requestingUserId
                ))
                .build());

        return toResponse(saved);
    }

    private boolean companyClientExists(String taxId) {
        return corporateCustomerRepository.existsByTaxId(taxId);
    }

    private CompanyClientResponse toResponse(CompanyClient company) {
        return CompanyClientResponse.builder()
                .id(company.getId())
                .legalName(company.getBusinessName())
                .taxId(company.getTaxId())
                .email(company.getEmail())
                .phone(company.getPhoneNumber())
                .address(company.getAddress())
                .legalRepresentativeId(company.getLegalRepresentativeId())
                .build();
    }
}