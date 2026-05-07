package com.banco.application.port.output;
import com.banco.application.dto.request.AuditLogRequest;
public interface AuditLogOutputPort {
    void log(AuditLogRequest logRequest);
}
