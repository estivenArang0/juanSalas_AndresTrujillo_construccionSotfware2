package com.banco.domain.service;

import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.Loan;
import com.banco.domain.model.valueobject.LoanStatus;
import org.springframework.stereotype.Service;

@Service
public class LoanDomainService {

    public void disburseLoanToAccount(Loan loan, BankAccount account) {
        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new IllegalStateException("Loan must be in APPROVED status for disbursement");
        }
        if (!account.isActive()) {
            throw new IllegalStateException("Target account must be active for disbursement");
        }
        
        // Update account balance
        account.credit(loan.getApprovedAmount());
        
        // Update loan status and details
        loan.disburse(account.getAccountNumber());
    }
}
