package com.banco.application.usecase;

import com.banco.application.dto.request.CreateCompanyClientRequest;
import com.banco.application.dto.request.CreateNaturalPersonRequest;
import com.banco.application.dto.response.CompanyClientResponse;
import com.banco.application.dto.response.NaturalPersonResponse;
import com.banco.application.dto.response.UserResponse;
import com.banco.application.port.input.UserInputPort;
import com.banco.domain.exception.DuplicateIdentificationException;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.model.entity.CompanyClient;
import com.banco.domain.model.entity.NaturalPersonClient;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.model.valueobject.UserStatus;
import com.banco.domain.repository.CompanyClientRepository;
import com.banco.domain.repository.NaturalPersonClientRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class UserUseCase implements UserInputPort {

    private final NaturalPersonClientRepository naturalPersonRepo;
    private final CompanyClientRepository companyClientRepo;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public NaturalPersonResponse registerNaturalPerson(CreateNaturalPersonRequest req) {
        if (naturalPersonRepo.existsByIdentificationNumber(req.getIdentificationNumber()))
            throw new DuplicateIdentificationException("Identification number already registered: " + req.getIdentificationNumber());
        if (userRepository.existsByUsername(req.getUsername()))
            throw new DuplicateIdentificationException("Username already taken: " + req.getUsername());

        // Validate age
        if (req.getBirthDate() != null && Period.between(req.getBirthDate(), LocalDate.now()).getYears() < 18)
            throw new IllegalArgumentException("Client must be at least 18 years old");

        NaturalPersonClient client = NaturalPersonClient.builder()
                .fullName(req.getFullName())
                .identificationNumber(req.getIdentificationNumber())
                .email(req.getEmail())
                .phone(req.getPhone())
                .birthDate(req.getBirthDate())
                .address(req.getAddress())
                .build();
        client.validateRequiredFields();
        NaturalPersonClient saved = naturalPersonRepo.save(client);

        User user = User.builder()
                .relatedEntityId(saved.getIdentificationNumber())
                .fullName(saved.getFullName())
                .identificationNumber(saved.getIdentificationNumber())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .birthDate(saved.getBirthDate())
                .address(saved.getAddress())
                .role(UserRole.NATURAL_PERSON_CLIENT)
                .status(UserStatus.ACTIVE)
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .build();
        userRepository.save(user);

        return NaturalPersonResponse.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .identificationNumber(saved.getIdentificationNumber())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .birthDate(saved.getBirthDate())
                .address(saved.getAddress())
                .username(req.getUsername())
                .build();
    }

    @Override
    @Transactional
    public CompanyClientResponse registerCompany(CreateCompanyClientRequest req) {
        if (companyClientRepo.existsByTaxId(req.getTaxId()))
            throw new DuplicateIdentificationException("Tax ID already registered: " + req.getTaxId());
        if (userRepository.existsByUsername(req.getUsername()))
            throw new DuplicateIdentificationException("Username already taken: " + req.getUsername());
        if (!naturalPersonRepo.existsByIdentificationNumber(req.getLegalRepresentativeId()))
            throw new ResourceNotFoundException("Legal representative not found: " + req.getLegalRepresentativeId());

        CompanyClient company = CompanyClient.builder()
                .legalName(req.getLegalName())
                .taxId(req.getTaxId())
                .email(req.getEmail())
                .phone(req.getPhone())
                .address(req.getAddress())
                .legalRepresentativeId(req.getLegalRepresentativeId())
                .build();
        company.validateRequiredFields();
        CompanyClient saved = companyClientRepo.save(company);

        User user = User.builder()
                .relatedEntityId(saved.getTaxId())
                .fullName(saved.getLegalName())
                .identificationNumber(saved.getTaxId())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .address(saved.getAddress())
                .role(UserRole.COMPANY_CLIENT)
                .status(UserStatus.ACTIVE)
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .build();
        userRepository.save(user);

        return CompanyClientResponse.builder()
                .id(saved.getId())
                .legalName(saved.getLegalName())
                .taxId(saved.getTaxId())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .address(saved.getAddress())
                .legalRepresentativeId(saved.getLegalRepresentativeId())
                .username(req.getUsername())
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .identificationNumber(user.getIdentificationNumber())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .status(user.getStatus())
                .username(user.getUsername())
                .build();
    }
}
