package com.banco.adapter.out.nosql;

import com.banco.adapter.out.nosql.document.AuditLogDocument;
import com.banco.adapter.out.nosql.repository.AuditLogMongoRepository;
import com.banco.domain.model.AuditLog;
import com.banco.domain.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuditLogRepositoryAdapter implements AuditLogRepository {
    private final AuditLogMongoRepository mongo;

    @Override
    public AuditLog save(AuditLog r) {
        AuditLogDocument doc = AuditLogDocument.builder()
                .auditLogId(r.getId())
                .operationType(r.getOperationType())
                .operationDateTime(r.getOperationDateTime())
                .userId(r.getUserId())
                .userRole(r.getUserRole())
                .affectedProductId(r.getAffectedProductId())
                .detailData(r.getDetails())
                .build();
        mongo.save(doc);
        return r;
    }

    @Override
    public List<AuditLog> buscarPorProductoAfectado(String idProducto) {
        return mongo.findByAffectedProductId(idProducto).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> buscarPorUsuario(Long userId) {
        return mongo.findByUserId(userId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> listarTodos() {
        return mongo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private AuditLog toDomain(AuditLogDocument d) {
        return AuditLog.builder()
                .id(d.getAuditLogId())
                .operationType(d.getOperationType())
                .operationDateTime(d.getOperationDateTime())
                .userId(d.getUserId())
                .userRole(d.getUserRole())
                .affectedProductId(d.getAffectedProductId())
                .details(d.getDetailData())
                .build();
    }
}
