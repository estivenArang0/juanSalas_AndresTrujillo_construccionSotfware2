package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.CreateCompanyClientRequest;
import com.banco.application.dto.response.CompanyClientResponse;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.DuplicateIdentificationException;
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
public class CompanyClientCreateService {

    private final CorporateCustomerRepository corporateCustomerRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public CompanyClientResponse create(CreateCompanyClientRequest request, Long requestingUserId) {

        // Solo empleados comerciales pueden crear clientes empresa
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.COMMERCIAL_EMPLOYEE)) {
            throw new UnauthorizedOperationException(
                    "Solo empleados comerciales pueden registrar clientes empresa");
        }

        // Verificar que no exista ya una empresa con ese NIT
        if (companyClientExists(request.getTaxId())) {
            throw new DuplicateIdentificationException(
                    "Ya existe una empresa registrada con el NIT: " + request.getTaxId());
        }

        // Usar el factory method de la entidad que ya tiene todas las validaciones
        CompanyClient newCompany = CompanyClient.create(
                request.getLegalName(),
                request.getTaxId(),
                request.getEmail(),
                request.getPhone(),
                request.getAddress(),
                request.getLegalRepresentativeId()
        );

        CompanyClient saved = corporateCustomerRepository.save(newCompany);

        auditLog.log(AuditLogRequest.builder()
                .operationType("COMPANY_CLIENT_CREATED")
                .operationDateTime(LocalDateTime.now())
                .userId(requestingUserId)
                .userRole(requestingUser.getRole().name())
                .affectedProductId(saved.getTaxId())
                .details(Map.of(
                        "legalName", request.getLegalName(),
                        "taxId", request.getTaxId(),
                        "legalRepresentativeId", request.getLegalRepresentativeId(),
                        "createdBy", requestingUserId
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