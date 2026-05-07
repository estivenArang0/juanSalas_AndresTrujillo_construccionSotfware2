package com.banco.application.usecase;

import com.banco.application.dto.request.AuditLogRequest;
import com.banco.application.dto.request.CreateTransferRequest;
import com.banco.application.dto.response.TransferResponse;
import com.banco.application.port.input.TransferInputPort;
import com.banco.application.port.output.AuditLogOutputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UnauthorizedOperationException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.Transfer;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.Money;
import com.banco.domain.model.valueobject.TransferStatus;
import com.banco.domain.model.valueobject.UserRole;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.TransferRepository;
import com.banco.domain.repository.UserRepository;
import com.banco.domain.service.TransferDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferUseCase implements TransferInputPort {

    private final TransferRepository transferRepository;
    private final BankAccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransferDomainService transferDomainService;
    private final AuditLogOutputPort auditLog;

    @Override
    @Transactional
    public TransferResponse createTransfer(CreateTransferRequest req, Long creatorUserId) {
        BankAccount source = accountRepository.findByAccountNumber(req.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found: " + req.getSourceAccountNumber()));
        BankAccount destination = accountRepository.findByAccountNumber(req.getDestinationAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found: " + req.getDestinationAccountNumber()));

        source.validateIsOperational();
        destination.validateIsOperational();

        Money amount = Money.of(req.getAmount(), req.getCurrency());
        if (!amount.isPositive()) throw new IllegalArgumentException("Transfer amount must be greater than zero");

        TransferStatus initialStatus = transferDomainService.determineInitialStatus(amount);

        Transfer transfer = Transfer.builder()
                .sourceAccountNumber(req.getSourceAccountNumber())
                .destinationAccountNumber(req.getDestinationAccountNumber())
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .status(initialStatus)
                .creatorUserId(creatorUserId)
                .build();

        // Execute immediately if below threshold
        if (initialStatus == TransferStatus.EXECUTED) {
            source.debit(amount);
            destination.credit(amount);
            accountRepository.save(source);
            accountRepository.save(destination);
        }

        Transfer saved = transferRepository.save(transfer);

        auditLog.log(AuditLogRequest.builder()
                .operationType("TRANSFER_CREATED")
                .operationDateTime(LocalDateTime.now())
                .userId(creatorUserId)
                .affectedProductId(String.valueOf(saved.getId()))
                .details(Map.of(
                        "sourceAccount", req.getSourceAccountNumber(),
                        "destinationAccount", req.getDestinationAccountNumber(),
                        "amount", req.getAmount(),
                        "status", initialStatus.name()
                )).build());

        return toResponse(saved);
    }

    @Override
    @Transactional
    public TransferResponse approveTransfer(Long transferId, Long supervisorId) {
        User supervisor = userRepository.findById(supervisorId)
                .orElseThrow(() -> new ResourceNotFoundException("Supervisor not found"));
        if (!supervisor.hasRole(UserRole.COMPANY_SUPERVISOR))
            throw new UnauthorizedOperationException("Only Company Supervisors can approve transfers");

        Transfer transfer = findTransfer(transferId);

        BankAccount source = accountRepository.findByAccountNumber(transfer.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));
        BankAccount destination = accountRepository.findByAccountNumber(transfer.getDestinationAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        source.validateIsOperational();
        Money balanceBefore = source.getBalance();
        Money destBefore = destination.getBalance();

        source.debit(transfer.getAmount());
        destination.credit(transfer.getAmount());
        transfer.execute(supervisorId);

        accountRepository.save(source);
        accountRepository.save(destination);
        Transfer saved = transferRepository.save(transfer);

        auditLog.log(AuditLogRequest.builder()
                .operationType("TRANSFER_APPROVED_AND_EXECUTED")
                .operationDateTime(LocalDateTime.now())
                .userId(supervisorId)
                .userRole(UserRole.COMPANY_SUPERVISOR.name())
                .affectedProductId(String.valueOf(transferId))
                .details(Map.of(
                        "amount", transfer.getAmount().getAmount(),
                        "balanceBeforeSource", balanceBefore.getAmount(),
                        "balanceAfterSource", source.getBalance().getAmount(),
                        "balanceBeforeDest", destBefore.getAmount(),
                        "balanceAfterDest", destination.getBalance().getAmount()
                )).build());

        return toResponse(saved);
    }

    @Override
    @Transactional
    public TransferResponse rejectTransfer(Long transferId, Long supervisorId) {
        User supervisor = userRepository.findById(supervisorId)
                .orElseThrow(() -> new ResourceNotFoundException("Supervisor not found"));
        if (!supervisor.hasRole(UserRole.COMPANY_SUPERVISOR))
            throw new UnauthorizedOperationException("Only Company Supervisors can reject transfers");

        Transfer transfer = findTransfer(transferId);
        transfer.reject(supervisorId);
        Transfer saved = transferRepository.save(transfer);

        auditLog.log(AuditLogRequest.builder()
                .operationType("TRANSFER_REJECTED")
                .operationDateTime(LocalDateTime.now())
                .userId(supervisorId)
                .userRole(UserRole.COMPANY_SUPERVISOR.name())
                .affectedProductId(String.valueOf(transferId))
                .details(Map.of("supervisorId", supervisorId))
                .build());

        return toResponse(saved);
    }

    @Override
    @Transactional
    public void processExpiredTransfers() {
        List<Transfer> pending = transferRepository.findByStatus(TransferStatus.PENDING_APPROVAL);
        for (Transfer t : pending) {
            if (t.isExpired(60)) {
                t.expire();
                transferRepository.save(t);
                auditLog.log(AuditLogRequest.builder()
                        .operationType("TRANSFER_EXPIRED")
                        .operationDateTime(LocalDateTime.now())
                        .affectedProductId(String.valueOf(t.getId()))
                        .details(Map.of(
                                "reason", "Approval not received within 60 minutes",
                                "createdAt", t.getCreatedAt().toString(),
                                "creatorUserId", t.getCreatorUserId()
                        )).build());
            }
        }
    }

    @Override
    public TransferResponse getTransferById(Long transferId) {
        return toResponse(findTransfer(transferId));
    }

    @Override
    public List<TransferResponse> getPendingTransfers() {
        return transferRepository.findByStatus(TransferStatus.PENDING_APPROVAL)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Transfer findTransfer(Long id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found: " + id));
    }

    private TransferResponse toResponse(Transfer t) {
        return TransferResponse.builder()
                .id(t.getId())
                .sourceAccountNumber(t.getSourceAccountNumber())
                .destinationAccountNumber(t.getDestinationAccountNumber())
                .amount(t.getAmount().getAmount())
                .currency(t.getAmount().getCurrency())
                .createdAt(t.getCreatedAt())
                .approvedAt(t.getApprovedAt())
                .status(t.getStatus())
                .creatorUserId(t.getCreatorUserId())
                .approverUserId(t.getApproverUserId())
                .build();
    }
}
