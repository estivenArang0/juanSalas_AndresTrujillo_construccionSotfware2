package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.TransferMapper;
import com.banco.adapter.out.persistence.repository.TransferJpaRepository;
import com.banco.domain.model.TransferStatus;
import com.banco.domain.model.Transfer;
import com.banco.domain.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransferRepositoryAdapter implements TransferRepository {
    private final TransferJpaRepository jpa;
    private final TransferMapper mapper;

    @Override public Transfer guardar(Transfer t) { return mapper.toDomain(jpa.save(mapper.toEntity(t))); }
    @Override public Optional<Transfer> buscarPorId(Long id) { return jpa.findById(id).map(mapper::toDomain); }
    @Override public List<Transfer> buscarPorCuentaOrigen(String c) { return jpa.findByCuentaOrigen(c).stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public List<Transfer> buscarPorEstado(TransferStatus e) {
        if (e == null) return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
        return jpa.findByEstadoTransferencia(e).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override public List<Transfer> buscarPorCuentaOrigenODestino(String o, String d) {
        return jpa.findByCuentaOrigenOrCuentaDestino(o, d).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
