package com.banco.adapter.out.persistence.mapper;

import com.banco.adapter.out.persistence.entity.BankAccountEntity;
import com.banco.domain.model.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {
    public BankAccount toDomain(BankAccountEntity e) {
        if (e == null) return null;
        return BankAccount.builder()
                .accountNumber(e.getNumeroCuenta()).accountType(e.getTipoCuenta())
                .holderId(e.getIdTitular()).currentBalance(e.getSaldoActual())
                .currency(e.getMoneda()).accountStatus(e.getEstadoCuenta())
                .openingDate(e.getFechaApertura()).build();
    }
    public BankAccountEntity toEntity(BankAccount d) {
        if (d == null) return null;
        return BankAccountEntity.builder()
                .accountNumber(d.getNumeroCuenta()).accountType(d.getTipoCuenta())
                .holderId(d.getIdTitular()).currentBalance(d.getSaldoActual())
                .currency(d.getMoneda()).accountStatus(d.getEstadoCuenta())
                .openingDate(d.getFechaApertura()).build();
    }
}
