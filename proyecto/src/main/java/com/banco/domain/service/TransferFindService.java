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

@Service
@RequiredArgsConstructor
public class TransferFindService {

    private final TransferRepository transferRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    public TransferResponse findById(Long transferId, Long requestingUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado: " + requestingUserId));

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transferencia no encontrada: " + transferId));

        // Clientes solo pueden ver sus propias transferencias
        if (requestingUser.hasRole(UserRole.NATURAL_PERSON_CLIENT) || 
            requestingUser.hasRole(UserRole.COMPANY_CLIENT)) {
            
            BankAccount source = bankAccountRepository.findByAccountNumber(transfer.getSourceAccountNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Cuenta de origen no encontrada"));
            
            if (!source.getOwnerId().equals(requestingUser.getIdentificationNumber())) {
                throw new UnauthorizedOperationException("No tienes permiso para consultar esta transferencia");
            }
        }

        return toResponse(transfer);
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
