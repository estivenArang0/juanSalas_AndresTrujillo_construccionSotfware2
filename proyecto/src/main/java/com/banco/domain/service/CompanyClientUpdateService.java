package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.UpdateCompanyClientRequest;
import com.banco.application.dto.response.CompanyClientResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.CompanyClient;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.CompanyClientRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyClientUpdateService {

    private final CompanyClientRepository companyClientRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public CompanyClientResponse update(String taxId, UpdateCompanyClientRequest request, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        // Solo clientes empresa o empleados comerciales/analistas pueden actualizar
        if (requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!taxId.equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para actualizar información de otra empresa");
            }
        } else if (!requestingUser.hasRole(UserRole.COMMERCIAL_EMPLOYEE) &&
                   !requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "No tienes permiso para actualizar información de empresas");
        }

        CompanyClient company = companyClientRepository.findByTaxId(taxId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empresa no encontrada con NIT: " + taxId));

        // Actualizar campos
        if (request.getLegalName() != null) company.setBusinessName(request.getLegalName());
        if (request.getEmail() != null) company.setEmail(request.getEmail());
        if (request.getPhone() != null) company.setPhone(request.getPhone());
        if (request.getAddress() != null) company.setAddress(request.getAddress());
        if (request.getLegalRepresentativeId() != null) company.setLegalRepresentativeId(request.getLegalRepresentativeId());

        CompanyClient saved = companyClientRepository.save(company);

        auditLog.log(AuditLogRequest.builder()
                .operationType("COMPANY_CLIENT_UPDATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(taxId)
                .details(Map.of(
                        "taxId", taxId,
                        "updatedBy", requestingUserId
                ))
                .build());

        return toResponse(saved);
    }

    private CompanyClientResponse toResponse(CompanyClient company) {
        return CompanyClientResponse.builder()
                .id(company.getId())
                .legalName(company.getBusinessName())
                .taxId(company.getTaxId())
                .email(company.getEmail())
                .phone(company.getPhone())
                .address(company.getAddress())
                .legalRepresentativeId(company.getLegalRepresentativeId())
                .build();
    }
}