package com.banco.domain.service;

import org.springframework.stereotype.Service;

@Service
public class LoanDisbursementService {
    // Only after approval, by Internal Analyst (Back-Office)
    // Must validate destination account is defined and active
    // Must update destination account balance
    // Must log disbursement and state change in Operation Log
    public void disburseLoan(/* parameters: loan, analyst, operationLog, ... */) {
        // 1. Validate loan is APPROVED
        // 2. Validate analyst role
        // 3. Validate destination account is defined and active
        // 4. Update account balance
        // 5. Change loan state to DISBURSED
        // 6. Register disbursement in operation log (who, when, state change)
        // TODO: Implement loan disbursement logic
    }
}
