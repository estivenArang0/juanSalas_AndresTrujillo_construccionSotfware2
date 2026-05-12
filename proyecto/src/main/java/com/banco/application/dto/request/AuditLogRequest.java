package com.banco.application.dto.request;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class AuditLogRequest {
    private String operationType;
    private LocalDateTime operationDateTime;
    private Long userId;
    private String userRole;
    private String affectedProductId;
    private Map<String, Object> details;


}
