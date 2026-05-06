package com.banco.adapter.out.persistence.repository;

import com.banco.adapter.out.persistence.entity.LoanEntity;
import com.banco.domain.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanJpaRepository extends JpaRepository<LoanEntity, Long> {
    List<LoanEntity> findByIdClienteSolicitante(String idCliente);
    List<LoanEntity> findByEstadoPrestamo(LoanStatus status);
}
