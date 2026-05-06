package com.banco.domain.service;

import org.springframework.stereotype.Service;

@Service
public class LoanApprovalService {
    // Only Internal Analyst can approve
    // Must log who approved, when, and state change in Operation Log
    public void approveLoan(/* parameters: loan, analyst, operationLog, ... */) {
        // 1. Validate analyst role
        // 2. Change loan state to APPROVED
        // 3. Register approval in operation log (who, when, state change)
        // TODO: Implement loan approval logic
    }
    public void rejectLoan(/* parameters */) {
        // TODO: Implement loan rejection logic
    }
}
