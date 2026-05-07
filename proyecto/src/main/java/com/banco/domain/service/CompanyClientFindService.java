package com.banco.domain.service;

import com.banco.application.dto.response.CompanyClientResponse;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.CompanyClient;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.CompanyClientRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyClientFindService {

    private final CompanyClientRepository companyClientRepository;
    private final UserRepository userRepository;

    public CompanyClientResponse findByTaxId(String taxId, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!taxId.equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para consultar información de otra empresa");
            }
        }

        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT)) {
            throw new UnauthorizedOperationException(
                    "No tienes permiso para consultar clientes empresa");
        }

        CompanyClient company = companyClientRepository.findByTaxId(taxId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una empresa registrada con el NIT: " + taxId));

        return toResponse(company);
    }

    public List<CompanyClientResponse> findAll(Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.COMMERCIAL_EMPLOYEE) &&
            !requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo empleados comerciales o analistas pueden listar todas las empresas");
        }

        List<CompanyClient> companies = companyClientRepository.findAll();

        if (companies.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No hay empresas registradas en el sistema");
        }

        return companies.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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