package com.banco.domain.service;

import com.banco.application.dto.response.CompanyClientResponse;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.CompanyClient;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.CorporateCustomerRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyClientFindService {

    private final CorporateCustomerRepository corporateCustomerRepository;
    private final UserRepository userRepository;

    // Buscar empresa por NIT
    public CompanyClientResponse findByTaxId(String taxId, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        // Clientes empresa solo pueden ver su propia información
        if (requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!taxId.equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para consultar información de otra empresa");
            }
        }

        // Clientes persona natural no pueden consultar empresas
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT)) {
            throw new UnauthorizedOperationException(
                    "No tienes permiso para consultar clientes empresa");
        }

        if (!companyClientExists(taxId)) {
            throw new ResourceNotFoundException(
                    "No existe una empresa registrada con el NIT: " + taxId);
        }

        CompanyClient company = corporateCustomerRepository.findByTaxId(taxId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una empresa registrada con el NIT: " + taxId));

        return toResponse(company);
    }

    // Listar todas las empresas — solo para empleados internos
    public List<CompanyClientResponse> findAll(Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.COMMERCIAL_EMPLOYEE) &&
            !requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo empleados comerciales o analistas pueden listar todas las empresas");
        }

        List<CompanyClient> companies = corporateCustomerRepository.findAll();

        if (companies.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No hay empresas registradas en el sistema");
        }

        return companies.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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