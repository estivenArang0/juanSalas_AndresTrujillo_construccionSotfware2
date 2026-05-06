package com.banco.adapter.out.nosql.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "bitacora_operaciones")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuditLogDocument {
    @Id
    private String auditLogId;
    private String operationType;
    private LocalDateTime operationDateTime;
    private Long userId;
    private String userRole;
    private String affectedProductId;
    private Map<String, Object> detailData;
}
