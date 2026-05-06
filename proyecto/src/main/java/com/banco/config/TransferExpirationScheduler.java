package com.banco.config;

import com.banco.domain.model.TransferStatus;
import com.banco.domain.model.Transfer;
import com.banco.domain.repository.TransferRepository;
import com.banco.domain.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class TransferExpirationScheduler {
    private final TransferRepository transferRepository;
    private final AuditLogService bitacoraService;

    @Value("${banco.transfer.tiempo-vencimiento-minutos:60}")
    private int minutosVencimiento;

    @Scheduled(fixedDelay = 60000) // cada minuto
    @Transactional
    public void vencerTransferenciasExpiradas() {
        List<Transfer> pendientes = transferRepository.buscarPorEstado(TransferStatus.PENDING_APPROVAL);
        for (Transfer t : pendientes) {
            if (t.isExpired(minutosVencimiento)) {
                t.expire();
                transferRepository.guardar(t);
                bitacoraService.registrarTransferenciaVencida(t);
                log.info("Transfer {} marcada como EXPIRED", t.getIdTransferencia());
            }
        }
    }
}
