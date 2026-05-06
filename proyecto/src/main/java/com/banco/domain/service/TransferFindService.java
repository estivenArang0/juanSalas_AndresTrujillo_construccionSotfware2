package com.banco.domain.service;

import com.banco.domain.model.entity.Transfer;
import com.banco.domain.repository.TransferRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TransferFindService {
    private final TransferRepository transferRepository;

    public TransferFindService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public Optional<Transfer> findById(Long id) {
        return transferRepository.buscarPorId(id);
    }

    public List<Transfer> findBySourceAccount(String sourceAccount) {
        return transferRepository.buscarPorCuentaOrigen(sourceAccount);
    }

    public List<Transfer> findByStatus(String status) {
        // Assuming status is a String, adapt if enum
        return transferRepository.buscarPorEstado(status);
    }

    public List<Transfer> findBySourceOrTargetAccount(String sourceAccount, String targetAccount) {
        return transferRepository.buscarPorCuentaOrigenODestino(sourceAccount, targetAccount);
    }

    public boolean transferExists(Long id) {
        return transferRepository.buscarPorId(id).isPresent();
    }
}
