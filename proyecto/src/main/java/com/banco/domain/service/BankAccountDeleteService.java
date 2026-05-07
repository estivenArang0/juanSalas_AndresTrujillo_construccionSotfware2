package com.banco.domain.service;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.AccountOperationNotAllowedException;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BankAccountDeleteService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final AuditLogOutputPort auditLog;

    public void delete(String accountNumber, Long requestingUserId) {

        // Solo analistas internos pueden eliminar cuentas
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        if (!requestingUser.hasRole(UserRole.INTERNAL_ANALYST)) {
            throw new UnauthorizedOperationException(
                    "Solo analistas internos pueden eliminar cuentas");
        }

        // Verificar que la cuenta existe
        if (!accountExists(accountNumber)) {
            throw new ResourceNotFoundException(
                    "Cuenta no encontrada: " + accountNumber);
        }

        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta no encontrada: " + accountNumber));

        // No se puede eliminar una cuenta con saldo positivo
        if (account.getBalance() != null && account.getBalance().isPositive()) {
            throw new AccountOperationNotAllowedException(
                    "No se puede eliminar la cuenta " + accountNumber +
                    " porque tiene saldo disponible: " + account.getBalance());
        }

        // No se puede eliminar una cuenta activa — debe estar bloqueada o cancelada primero
        if (account.isActive()) {
            throw new AccountOperationNo