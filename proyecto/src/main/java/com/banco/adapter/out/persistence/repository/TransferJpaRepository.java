package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.TransferJpaEntity;
import com.banco.domain.model.valueobject.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TransferJpaRepository extends JpaRepository<TransferJpaEntity, Long> {
    List<TransferJpaEntity> findBySourceAccountNumber(String sourceAccountNumber);
    List<TransferJpaEntity> findByStatus(TransferStatus status);
    @Query("SELECT t FROM TransferJpaEntity t WHERE t.sourceAccountNumber = :origen OR t.destinationAccountNumber = :destino")
    List<TransferJpaEntity> findBySourceAccountNumberOrDestinationAccountNumber(@Param("origen") String origen, @Param("destino") String destino);
}
