package com.banco.domain.service;

import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.OperationLog;
import com.banco.domain.model.entity.Transfer;
import com.banco.domain.model.valueobject.TransferStatus;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class TransferUpdateService {
    // Only Company Supervisor/Admin can approve high-value transfers
    public void approveTransfer(Transfer transfer, BankAccount source, BankAccount destination, String approverRole, OperationLog operationLog) {
        if (!isSupervisorOrAdmin(approverRole)) {
            throw new SecurityException("Only Company Supervisor or Admin can approve transfers");
        }
        if (!source.hasSufficientFunds(transfer.getAmount())) {
            throw new IllegalStateException("Insufficient funds in source account");
        }
        // Execute transfer (domain logic)
        transfer.execute();
        source.debit(transfer.getAmount());
        destination.credit(transfer.getAmount());
        transfer.setStatus(TransferStatus.EXECUTED);
        // Log operation
        operationLog.logApproval(transfer, approverRole, LocalDateTime.now());
    }

    // Only Company Supervisor/Admin can reject high-value transfers
    public void rejectTransfer(Transfer transfer, String approverRole, OperationLog operationLog) {
        if (!isSupervisorOrAdmin(approverRole)) {
            throw new SecurityException("Only Company Supervisor or Admin can reject transfers");
        }
        transfer.reject();
        operationLog.logRejection(transfer, approverRole, LocalDateTime.now());
    }

    // Expire transfer if pending approval for more than 60 minutes
    public void expireTransfer(Transfer transfer, OperationLog operationLog) {
        if (transfer.getStatus() == TransferStatus.PENDING_APPROVAL &&
            transfer.getCreatedAt().plusMinutes(60).isBefore(LocalDateTime.now())) {
            transfer.expire();
            operationLog.logExpiration(transfer, "expired due to lack of approval", LocalDateTime.now());
        }
    }

    private boolean isSupervisorOrAdmin(String role) {
        return "SUPERVISOR".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
    }
}
