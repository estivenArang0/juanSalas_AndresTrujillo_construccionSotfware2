package com.banco.domain.service;

import com.banco.application.dto.response.BankAccountResponse;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountFindService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    // Buscar por número de cuenta
    public BankAccountResponse findByAccountNumber(String accountNumber, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta no encontrada: " + accountNumber));

        // Un cliente solo puede ver sus propias cuentas
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT) ||
            requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!account.getOwnerId().equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para consultar esta cuenta");
            }
        }

        return toResponse(account);
    }

    // Buscar todas las cuentas de un cliente
    public List<BankAccountResponse> findByOwnerId(String ownerId, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        // Un cliente solo puede ver sus propias cuentas
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT) ||
            requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!ownerId.equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para consultar las cuentas de otro cliente");
            }
        }

        if (!accountExists(ownerId)) {
            throw new ResourceNotFoundException(
                    "No se encontraron cuentas para el cliente: " + ownerId);
        }

        return bankAccountRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private boolean accountExists(String ownerId) {
        return !bankAccountRepository.findByOwnerId(ownerId).isEmpty();
    }

    private BankAccountResponse toResponse(BankAccount account) {
        return BankAccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .ownerId(account.getOwnerId())
                .balance(account.getBalance().getAmount())
                .currency(account.getBalance().getCurrency())
                .status(account.getStatus())
                .openingDate(account.getOpeningDate())
                .build();
    }
}