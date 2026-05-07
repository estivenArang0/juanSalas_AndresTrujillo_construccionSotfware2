package com.banco.domain.model;

import java.time.LocalDateTime;
import java.util.Map;

public class AuditLog {
    private String id;
    private String operationType;
    private LocalDateTime operationDateTime;
    private Long userId;
    private String userRole;
    private String affectedProductId;
    private Map<String, Object> details;

    public AuditLog() {}

    public AuditLog(String id, String operationType, LocalDateTime operationDateTime, Long userId, String userRole, String affectedProductId, Map<String, Object> details) {
        this.id = id;
        this.operationType = operationType;
        this.operationDateTime = operationDateTime;
        this.userId = userId;
        this.userRole = userRole;
        this.affectedProductId = affectedProductId;
        this.details = details;
    }

    public static AuditLogBuilder builder() { return new AuditLogBuilder(); }

    public static class AuditLogBuilder {
        private AuditLog l = new AuditLog();
        public AuditLogBuilder id(String id) { l.id = id; return this; }
        public AuditLogBuilder operationType(String t) { l.operationType = t; return this; }
        public AuditLogBuilder operationDateTime(LocalDateTime t) { l.operationDateTime = t; return this; }
        public AuditLogBuilder userId(Long id) { l.userId = id; return this; }
        public AuditLogBuilder userRole(String role) { l.userRole = role; return this; }
        public AuditLogBuilder affectedProductId(String id) { l.affectedProductId = id; return this; }
        public AuditLogBuilder details(Map<String, Object> d) { l.details = d; return this; }
        public AuditLog build() { return l; }
    }

    public String getId() { return id; }
    public String getOperationType() { return operationType; }
    public LocalDateTime getOperationDateTime() { return operationDateTime; }
    public Long getUserId() { return userId; }
    public String getUserRole() { return userRole; }
    public String getAffectedProductId() { return affectedProductId; }
    public Map<String, Object> getDetails() { return details; }

    public static AuditLog create(String operationType, String affectedProductId, Long userId, String details) {
        return AuditLog.builder()
                .operationType(operationType)
                .affectedProductId(affectedProductId)
                .userId(userId)
                .operationDateTime(LocalDateTime.now())
                .details(Map.of("message", details))
                .build();
    }
}
