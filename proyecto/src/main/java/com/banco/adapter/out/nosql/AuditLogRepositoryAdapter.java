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
    public AuditLog registrar(AuditLog r) {
        AuditLogDocument doc = AuditLogDocument.builder()
                .auditLogId(r.getIdBitacora()).operationType(r.getTipoOperacion())
                .operationDateTime(r.getFechaHoraOperacion()).userId(r.getIdUsuario())
                .userRole(r.getRolUsuario()).affectedProductId(r.getIdProductoAfectado())
                .detailData(r.getDatosDetalle()).build();
        mongo.save(doc);
        return r;
    }

    @Override
    public List<AuditLog> buscarPorProductoAfectado(String idProducto) {
        return mongo.findByIdProductoAfectado(idProducto).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> buscarPorUsuario(Long userId) {
        return mongo.findByIdUsuario(userId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> listarTodos() {
        return mongo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private AuditLog toDomain(AuditLogDocument d) {
        return AuditLog.builder()
                .auditLogId(d.getIdBitacora()).operationType(d.getTipoOperacion())
                .operationDateTime(d.getFechaHoraOperacion()).userId(d.getIdUsuario())
                .userRole(d.getRolUsuario()).affectedProductId(d.getIdProductoAfectado())
                .detailData(d.getDatosDetalle()).build();
    }
}
