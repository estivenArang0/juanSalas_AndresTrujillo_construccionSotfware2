package com.bank.app.application.usecase;

import com.bank.app.application.dto.request.CreateCompanyClientRequest;
import com.bank.app.application.dto.request.CreateNaturalPersonRequest;
import com.bank.app.application.dto.response.CompanyClientResponse;
import com.bank.app.application.dto.response.NaturalPersonResponse;
import com.bank.app.application.dto.response.UserResponse;
import com.bank.app.application.port.input.UserInputPort;
import com.bank.app.domain.exception.DuplicateIdentificationException;
import com.bank.app.domain.exception.ResourceNotFoundException;
import com.bank.app.domain.model.entity.CompanyClient;
import com.bank.app.domain.model.entity.NaturalPersonClient;
import com.bank.app.domain.model.entity.User;
import com.bank.app.domain.model.valueobject.UserRole;
import com.bank.app.domain.model.valueobject.UserStatus;
import com.bank.app.domain.repository.CompanyClientRepository;
import com.bank.app.domain.repository.NaturalPersonClientRepository;
import com.bank.app.domain.repository.UserRepository;
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
