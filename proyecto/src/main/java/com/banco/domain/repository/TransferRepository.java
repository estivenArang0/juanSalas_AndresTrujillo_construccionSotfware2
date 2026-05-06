package com.banco.domain.repository;

import com.banco.domain.model.TransferStatus;
import com.banco.domain.model.Transfer;
import java.util.List;
import java.util.Optional;

public interface TransferRepository {
    Transfer guardar(Transfer transfer);
    Optional<Transfer> buscarPorId(Long id);
    List<Transfer> buscarPorCuentaOrigen(String sourceAccount);
    List<Transfer> buscarPorEstado(TransferStatus status);
    List<Transfer> buscarPorCuentaOrigenODestino(String sourceAccount, String targetAccount);
}
