package com.bank.app.adapter.out.persistence.adapter;

import com.bank.app.adapter.out.persistence.mapper.TransferMapper;
import com.bank.app.adapter.out.persistence.repository.TransferJpaRepository;
import com.bank.app.domain.model.entity.Transfer;
import com.bank.app.domain.model.valueobject.TransferStatus;
import com.bank.app.domain.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransferRepositoryAdapter implements TransferRepository {
    private final TransferJpaRepository jpaRepository;
    private final TransferMapper mapper;

    @Override public Transfer save(Transfer t) { return mapper.toDomain(jpaRepository.save(mapper.toJpa(t))); }
    @Override public Optional<Transfer> findById(Long id) { return jpaRepository.findById(id).map(mapper::toDomain); }
    @Override public List<Transfer> findByStatus(TransferStatus s) { return jpaRepository.findByStatus(s).stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public List<Transfer> findByCreatorUserId(Long userId) { return jpaRepository.findByCreatorUserId(userId).stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public List<Transfer> findBySourceAccountNumber(String n) { return jpaRepository.findBySourceAccountNumber(n).stream().map(mapper::toDomain).collect(Collectors.toList()); }
    @Override public List<Transfer> findPendingExpired(int minutes) {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(minutes);
        return jpaRepository.findExpiredPendingTransfers(expirationTime).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
