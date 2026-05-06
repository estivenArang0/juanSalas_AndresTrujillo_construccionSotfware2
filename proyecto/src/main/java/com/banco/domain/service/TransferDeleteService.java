package com.banco.domain.service;

import com.banco.domain.model.entity.Transfer;
import com.banco.domain.repository.TransferRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TransferDeleteService {
    private final TransferRepository transferRepository;

    public TransferDeleteService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public boolean delete(Long transferId) {
        Optional<Transfer> transferOpt = transferRepository.buscarPorId(transferId);
        if (transferOpt.isPresent()) {
            Transfer transfer = transferOpt.get();
            // Domain logic: only allow delete if not executed
            if (!transfer.isExecuted()) {
                transferRepository.eliminar(transferId);
                return true;
            }
        }
        return false;
    }

    public boolean transferExists(Long id) {
        return transferRepository.buscarPorId(id).isPresent();
    }
}
