package com.banco.adapter.out.persistence;

import com.banco.adapter.out.persistence.mapper.TransferMapper;
import com.banco.adapter.out.persistence.repository.TransferJpaRepository;
import com.banco.domain.model.entity.Transfer;
import com.banco.domain.repository.TransferRepository;
import com.banco.domain.model.valueobject.TransferStatus;
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

    @Override
    public Transfer save(Transfer transfer) {
        return mapper.toDomain(jpa.save(mapper.toEntity(transfer)));
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Transfer> findBySourceAccountNumber(String sourceAccountNumber) {
        return jpa.findBySourceAccountNumber(sourceAccountNumber).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByStatus(TransferStatus status) {
        return jpa.findByStatus(status).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findBySourceAccountNumberOrDestinationAccountNumber(String sourceAccount, String targetAccount) {
        return jpa.findBySourceAccountNumberOrDestinationAccountNumber(sourceAccount, targetAccount).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findAllByStatus(TransferStatus status) {
        return jpa.findByStatus(status).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByAccountNumber(String accountNumber) {
        return jpa.findBySourceAccountNumberOrDestinationAccountNumber(accountNumber, accountNumber).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
