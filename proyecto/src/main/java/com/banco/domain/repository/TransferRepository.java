package com.banco.domain.repository;

import com.banco.domain.model.valueobject.TransferStatus;
import com.banco.domain.model.entity.Transfer;
import java.util.List;
import java.util.Optional;

public interface TransferRepository {
    Transfer save(Transfer transfer);
    Optional<Transfer> findById(Long id);
    List<Transfer> findBySourceAccountNumber(String sourceAccountNumber);
    List<Transfer> findByStatus(TransferStatus status);
    List<Transfer> findBySourceAccountNumberOrDestinationAccountNumber(String sourceAccount, String targetAccount);
}
