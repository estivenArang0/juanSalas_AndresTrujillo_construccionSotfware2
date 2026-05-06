package com.bank.app.application.port.input;
import com.bank.app.application.dto.request.CreateTransferRequest;
import com.bank.app.application.dto.response.TransferResponse;
import java.util.List;
public interface TransferInputPort {
    TransferResponse createTransfer(CreateTransferRequest request, Long creatorUserId);
    TransferResponse approveTransfer(Long transferId, Long supervisorId);
    TransferResponse rejectTransfer(Long transferId, Long supervisorId);
    TransferResponse getTransferById(Long transferId);
    List<TransferResponse> getPendingTransfers();
    void processExpiredTransfers();
}
