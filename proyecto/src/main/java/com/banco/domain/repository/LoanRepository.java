package com.banco.domain.repository;

import com.banco.domain.model.LoanStatus;
import com.banco.domain.model.Loan;
import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan guardar(Loan prestamo);
    Optional<Loan> buscarPorId(Long id);
    List<Loan> buscarPorCliente(String idCliente);
    List<Loan> buscarPorEstado(LoanStatus status);
    List<Loan> listarTodos();
}
