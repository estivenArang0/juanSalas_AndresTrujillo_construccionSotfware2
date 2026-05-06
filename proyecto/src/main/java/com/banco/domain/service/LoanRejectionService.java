package com.banco.domain.service;

import org.springframework.stereotype.Service;

@Service
public class LoanRejectionService {
    // Only Internal Analyst can reject
    // Must log who rejected, when, and state change in Operation Log
    public void rejectLoan(/* parameters: loan, analyst, operationLog, ... */) {
        // 1. Validate analyst role
        // 2. Change loan state to REJECTED
        // 3. Register rejection in operation log (who, when, state change)
        // TODO: Implement loan rejection logic
    }
}
