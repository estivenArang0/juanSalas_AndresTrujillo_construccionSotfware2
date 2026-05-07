package com.banco.application.port.input;
import com.banco.application.dto.request.CreateTransferRequest;
import com.banco.application.dto.response.TransferResponse;
import java.util.List;
public interface TransferInputPort {
    TransferResponse createTransfer(CreateTransferRequest request, Long creatorUserId);
    TransferResponse approveTransfer(Long transferId, Long supervisorId);
    TransferResponse rejectTransfer(Long transferId, Long supervisorId);
    TransferResponse getTransferById(Long transferId);
    List<TransferResponse> getPendingTransfers();
    void processExpiredTransfers();
}
