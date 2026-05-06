package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.LoanMapper;
import com.banco.adapter.out.persistence.repository.LoanJpaRepository;
import com.banco.domain.model.LoanStatus;
import com.banco.domain.model.Loan;
import com.banco.domain.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoanRepositoryAdapter implements LoanRepository {
    private final LoanJpaRepository jpa;
    private final LoanMapper mapper;

    @Override public Loan guardar(Loan p) { return mapper.toDomain(jpa.save(mapper.toEntity(p))); }
    @Override public Optional<Loan> buscarPorId(Long id) { return jpa.findById(id).map(mapper::toDomain); }
    @Override public List<Loan> buscarPorCliente(String id) { return jpa.findByIdClienteSolicitante(id).stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public List<Loan> buscarPorEstado(LoanStatus e) { return jpa.findByEstadoPrestamo(e).stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public List<Loan> listarTodos() { return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList()); }
}
