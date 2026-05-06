package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.TransferEntity;
import com.banco.domain.model.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TransferJpaRepository extends JpaRepository<TransferEntity, Long> {
    List<TransferEntity> findByCuentaOrigen(String sourceAccount);
    List<TransferEntity> findByEstadoTransferencia(TransferStatus status);
    @Query("SELECT t FROM TransferEntity t WHERE t.sourceAccount = :origen OR t.targetAccount = :destino")
    List<TransferEntity> findByCuentaOrigenOrCuentaDestino(@Param("origen") String origen, @Param("destino") String destino);
}
