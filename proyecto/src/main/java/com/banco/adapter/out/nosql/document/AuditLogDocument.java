package com.banco.adapter.out.nosql.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "bitacora_operaciones")
public class AuditLogDocument {
    @Id
    private String auditLogId;
    private String operationType;
    private LocalDateTime operationDateTime;
    private Long userId;
    private String userRole;
    private String affectedProductId;
    private Map<String, Object> detailData;

    public AuditLogDocument() {}

    public AuditLogDocument(String auditLogId, String operationType, LocalDateTime operationDateTime, Long userId, String userRole, String affectedProductId, Map<String, Object> detailData) {
        this.auditLogId = auditLogId;
        this.operationType = operationType;
        this.operationDateTime = operationDateTime;
        this.userId = userId;
        this.userRole = userRole;
        this.affectedProductId = affectedProductId;
        this.detailData = detailData;
    }

    public static AuditLogDocumentBuilder builder() { return new AuditLogDocumentBuilder(); }

    public static class AuditLogDocumentBuilder {
        private AuditLogDocument d = new AuditLogDocument();
        public AuditLogDocumentBuilder auditLogId(String id) { d.auditLogId = id; return this; }
        public AuditLogDocumentBuilder operationType(String t) { d.operationType = t; return this; }
        public AuditLogDocumentBuilder operationDateTime(LocalDateTime t) { d.operationDateTime = t; return this; }
        public AuditLogDocumentBuilder userId(Long id) { d.userId = id; return this; }
        public AuditLogDocumentBuilder userRole(String role) { d.userRole = role; return this; }
        public AuditLogDocumentBuilder affectedProductId(String id) { d.affectedProductId = id; return this; }
        public AuditLogDocumentBuilder detailData(Map<String, Object> data) { d.detailData = data; return this; }
        public AuditLogDocument build() { return d; }
    }

    public String getAuditLogId() { return auditLogId; }
    public void setAuditLogId(String auditLogId) { this.auditLogId = auditLogId; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public LocalDateTime getOperationDateTime() { return operationDateTime; }
    public void setOperationDateTime(LocalDateTime operationDateTime) { this.operationDateTime = operationDateTime; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public String getAffectedProductId() { return affectedProductId; }
    public void setAffectedProductId(String affectedProductId) { this.affectedProductId = affectedProductId; }
    public Map<String, Object> getDetailData() { return detailData; }
    public void setDetailData(Map<String, Object> detailData) { this.detailData = detailData; }
}
