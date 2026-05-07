package com.banco.domain.service;

import com.banco.domain.model.AuditLog;
import com.banco.domain.model.entity.Transfer;
import com.banco.domain.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public void registrarTransferenciaVencida(Transfer t) {
        AuditLog log = AuditLog.create(
            "TRANSFER_EXPIRED",
            t.getId() != null ? t.getId().toString() : "UNKNOWN",
            t.getCreatorUserId(),
            "Transfer expired due to timeout. Source: " + t.getSourceAccountNumber()
        );
        auditLogRepository.save(log);
    }
    
    public void save(AuditLog log) {
        auditLogRepository.save(log);
    }
}
