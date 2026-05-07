package com.banco.domain.service;

import com.banco.application.dto.response.BankAccountResponse;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.Money;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountQueryService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    // Buscar cuenta por número (equivalente a buscar por ID en tu modelo)
    public BankAccountResponse getBankAccountById(String accountNumber, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta no encontrada: " + accountNumber));

        // Clientes solo pueden ver sus propias cuentas
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT) ||
            requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!account.getOwnerId().equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para consultar esta cuenta");
            }
        }

        return toResponse(account);
    }

    // Listar todas las cuentas de un cliente
    public List<BankAccountResponse> listBankAccountsByClient(String ownerId, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        // Clientes solo pueden listar sus propias cuentas
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT) ||
            requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!ownerId.equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para consultar las cuentas de otro cliente");
            }
        }

        List<BankAccount> accounts = bankAccountRepository.findByOwnerId(ownerId);

        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No se encontraron cuentas para el cliente: " + ownerId);
        }

        return accounts.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Consultar saldo de una cuenta
    public BigDecimal getAccountBalance(String accountNumber, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta no encontrada: " + accountNumber));

        // Clientes solo pueden consultar el saldo de sus propias cuentas
        // Empleados de ventanilla también pueden consultar saldos para transacciones de caja
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT) ||
            requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!account.getOwnerId().equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException(
                        "No tienes permiso para consultar el saldo de esta cuenta");
            }
        }

        Money balance = account.getBalance();
        if (balance == null) {
            return BigDecimal.ZERO;
        }

        return balance.getAmount();
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