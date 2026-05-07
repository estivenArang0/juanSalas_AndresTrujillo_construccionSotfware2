package com.banco.domain.service;

import com.banco.application.dto.response.NaturalPersonResponse;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.NaturalPersonClient;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.NaturalPersonClientRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NaturalPersonClientFindService {

    private final NaturalPersonClientRepository naturalPersonClientRepository;
    private final UserRepository userRepository;

    public NaturalPersonResponse findByIdentificationNumber(String idNum, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT)) {
            if (!idNum.equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para consultar información de otro cliente");
            }
        }

        NaturalPersonClient client = naturalPersonClientRepository.findByIdentificationNumber(idNum)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con identificación: " + idNum));

        return toResponse(client);
    }

    public List<NaturalPersonResponse> findAll(Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.COMMERCIAL_EMPLOYEE) &&
            !requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo empleados comerciales o analistas pueden listar todos los clientes");
        }

        return naturalPersonClientRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private NaturalPersonResponse toResponse(NaturalPersonClient client) {
        return NaturalPersonResponse.builder()
                .id(client.getId())
                .fullName(client.getFullName())
                .identificationNumber(client.getIdentificationNumber())
                .email(client.getEmail())
                .phone(client.getPhone())
                .birthDate(client.getBirthDate())
                .address(client.getAddress())
                .build();
    }
}
