package com.bank.app.application.dto.request;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;
@Data @Builder
public class AuditLogRequest {
    private String operationType;
    private LocalDateTime operationDateTime;
    private Long userId;
    private String userRole;
    private String affectedProductId;
    private Map<String, Object> details;
}
