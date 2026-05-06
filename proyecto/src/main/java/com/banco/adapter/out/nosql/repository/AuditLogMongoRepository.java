package com.banco.adapter.out.nosql.repository;

import com.banco.adapter.out.nosql.document.AuditLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {
    List<AuditLogDocument> findByIdProductoAfectado(String idProducto);
    List<AuditLogDocument> findByIdUsuario(Long userId);
}
