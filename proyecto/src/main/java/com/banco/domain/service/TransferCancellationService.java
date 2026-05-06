package com.banco.domain.service;

import org.springframework.stereotype.Service;

@Service
public class TransferCancellationService {
    // Only allowed if transfer is not executed
    // Must log cancellation in Operation Log
    public void cancelTransfer(/* parameters: transfer, user, operationLog, ... */) {
        // 1. Validate transfer state
        // 2. Validate user role
        // 3. Set state to CANCELLED
        // 4. Register cancellation in operation log
        // TODO: Implement cancellation logic
    }
}
