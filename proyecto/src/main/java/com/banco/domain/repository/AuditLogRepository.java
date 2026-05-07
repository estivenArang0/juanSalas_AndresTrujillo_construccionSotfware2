package com.banco.domain.repository;

import com.banco.domain.model.AuditLog;
import java.util.List;

public interface AuditLogRepository {
    AuditLog save(AuditLog registro);
    List<AuditLog> buscarPorProductoAfectado(String idProducto);
    List<AuditLog> buscarPorUsuario(Long userId);
    List<AuditLog> listarTodos();
}
