package com.bank.app.application.port.output;
import com.bank.app.application.dto.request.AuditLogRequest;
public interface AuditLogOutputPort {
    void log(AuditLogRequest logRequest);
}
