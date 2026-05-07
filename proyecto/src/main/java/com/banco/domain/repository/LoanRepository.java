package com.banco.domain.repository;

import com.banco.domain.model.valueobject.LoanStatus;
import com.banco.domain.model.entity.Loan;
import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan save(Loan loan);
    Optional<Loan> findById(Long id);
    List<Loan> findByClientId(String clientId);
    List<Loan> findByStatus(LoanStatus status);
    List<Loan> findAll();
}
