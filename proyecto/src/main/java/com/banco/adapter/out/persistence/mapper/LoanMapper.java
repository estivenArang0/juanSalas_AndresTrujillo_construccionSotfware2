package com.banco.adapter.out.persistence.mapper;

import com.banco.adapter.out.persistence.entity.LoanEntity;
import com.banco.domain.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {
    public Loan toDomain(LoanEntity e) {
        if (e == null) return null;
        return Loan.builder()
                .loanId(e.getIdPrestamo()).loanType(e.getTipoPrestamo())
                .applicantCustomerId(e.getIdClienteSolicitante()).requestedAmount(e.getMontoSolicitado())
                .approvedAmount(e.getMontoAprobado()).interestRate(e.getTasaInteres())
                .termMonths(e.getPlazoMeses()).loanStatus(e.getEstadoPrestamo())
                .approvalDate(e.getFechaAprobacion()).disbursementDate(e.getFechaDesembolso())
                .disbursementTargetAccount(e.getCuentaDestinoDesembolso())
                .approverAnalystId(e.getIdAnalistaAprobador()).creatorUserId(e.getIdUsuarioCreador()).build();
    }
    public LoanEntity toEntity(Loan d) {
        if (d == null) return null;
        return LoanEntity.builder()
                .loanId(d.getIdPrestamo()).loanType(d.getTipoPrestamo())
                .applicantCustomerId(d.getIdClienteSolicitante()).requestedAmount(d.getMontoSolicitado())
                .approvedAmount(d.getMontoAprobado()).interestRate(d.getTasaInteres())
                .termMonths(d.getPlazoMeses()).loanStatus(d.getEstadoPrestamo())
                .approvalDate(d.getFechaAprobacion()).disbursementDate(d.getFechaDesembolso())
                .disbursementTargetAccount(d.getCuentaDestinoDesembolso())
                .approverAnalystId(d.getIdAnalistaAprobador()).creatorUserId(d.getIdUsuarioCreador()).build();
    }
}
