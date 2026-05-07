package com.banco.application.dto.request;

import java.time.LocalDateTime;
import java.util.Map;

public class AuditLogRequest {
    private String operationType;
    private LocalDateTime operationDateTime;
    private Long userId;
    private String userRole;
    private String affectedProductId;
    private Map<String, Object> details;

    public AuditLogRequest() {}

    public AuditLogRequest(String operationType, LocalDateTime operationDateTime, Long userId, String userRole, String affectedProductId, Map<String, Object> details) {
        this.operationType = operationType;
        this.operationDateTime = operationDateTime;
        this.userId = userId;
        this.userRole = userRole;
        this.affectedProductId = affectedProductId;
        this.details = details;
    }

    public static AuditLogRequestBuilder builder() { return new AuditLogRequestBuilder(); }

    public static class AuditLogRequestBuilder {
        private AuditLogRequest r = new AuditLogRequest();
        public AuditLogRequestBuilder operationType(String t) { r.operationType = t; return this; }
        public AuditLogRequestBuilder operationDateTime(LocalDateTime t) { r.operationDateTime = t; return this; }
        public AuditLogRequestBuilder userId(Long id) { r.userId = id; return this; }
        public AuditLogRequestBuilder userRole(String role) { r.userRole = role; return this; }
        public AuditLogRequestBuilder affectedProductId(String id) { r.affectedProductId = id; return this; }
        public AuditLogRequestBuilder details(Map<String, Object> d) { r.details = d; return this; }
        public AuditLogRequest build() { return r; }
    }

    public String getOperationType() { return operationType; }
    public LocalDateTime getOperationDateTime() { return operationDateTime; }
    public Long getUserId() { return userId; }
    public String getUserRole() { return userRole; }
    public String getAffectedProductId() { return affectedProductId; }
    public Map<String, Object> getDetails() { return details; }
}
