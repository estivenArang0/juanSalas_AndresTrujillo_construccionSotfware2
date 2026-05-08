package com.banco.domain.service;

import com.banco.application.dto.response.TransferResponse;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.Transfer;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.TransferRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferQueryService {

    private final TransferRepository transferRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    public List<TransferResponse> getTransfersByAccount(String accountNumber, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta no encontrada: " + accountNumber));

        // Permisos: Clientes solo pueden ver transferencias de sus propias cuentas
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT) || 
            requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            if (!account.getOwnerId().equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException("No tienes permiso para consultar transferencias de esta cuenta");
            }
        }

        List<Transfer> transfers = transferRepository.findByAccountNumber(accountNumber);

        return transfers.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TransferResponse toResponse(Transfer transfer) {
        return TransferResponse.builder()
                .id(transfer.getId())
                .sourceAccountNumber(transfer.getSourceAccountNumber())
                .destinationAccountNumber(transfer.getDestinationAccountNumber())
                .amount(transfer.getAmount().getAmount())
                .currency(transfer.getAmount().getCurrency())
                .description(transfer.getDescription())
                .status(transfer.getStatus())
                .requestedAt(transfer.getRequestedAt())
                .executedAt(transfer.getExecutedAt())
                .build();
    }
}
