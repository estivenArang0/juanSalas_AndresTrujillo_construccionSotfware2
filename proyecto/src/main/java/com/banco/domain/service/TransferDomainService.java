package com.banco.domain.service;

import com.banco.domain.model.valueobject.Money;
import com.banco.domain.model.valueobject.TransferStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferDomainService {

    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("1000000"); // 1 Million

    public TransferStatus determineInitialStatus(Money amount) {
        if (amount.getAmount().compareTo(HIGH_VALUE_THRESHOLD) > 0) {
            return TransferStatus.PENDING_APPROVAL;
        }
        return TransferStatus.EXECUTED;
    }
}
