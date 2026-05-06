package com.banco.adapter.out.persistence.mapper;

import com.banco.adapter.out.persistence.entity.TransferEntity;
import com.banco.domain.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {
    public Transfer toDomain(TransferEntity e) {
        if (e == null) return null;
        return Transfer.builder()
                .transferId(e.getIdTransferencia()).sourceAccount(e.getCuentaOrigen())
                .targetAccount(e.getCuentaDestino()).amount(e.getMonto())
                .creationDate(e.getFechaCreacion()).approvalDate(e.getFechaAprobacion())
                .transferStatus(e.getEstadoTransferencia()).creatorUserId(e.getIdUsuarioCreador())
                .approverUserId(e.getIdUsuarioAprobador()).build();
    }
    public TransferEntity toEntity(Transfer d) {
        if (d == null) return null;
        return TransferEntity.builder()
                .transferId(d.getIdTransferencia()).sourceAccount(d.getCuentaOrigen())
                .targetAccount(d.getCuentaDestino()).amount(d.getMonto())
                .creationDate(d.getFechaCreacion()).approvalDate(d.getFechaAprobacion())
                .transferStatus(d.getEstadoTransferencia()).creatorUserId(d.getIdUsuarioCreador())
                .approverUserId(d.getIdUsuarioAprobador()).build();
    }
}
