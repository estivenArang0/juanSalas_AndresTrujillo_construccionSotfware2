package com.banco.application.port.input;
import com.banco.application.dto.request.CreateBankAccountRequest;
import com.banco.application.dto.response.BankAccountResponse;
import java.util.List;
public interface BankAccountInputPort {
    BankAccountResponse openAccount(CreateBankAccountRequest request);
    BankAccountResponse getAccountByNumber(String accountNumber);
    List<BankAccountResponse> getAccountsByOwner(String ownerId);
}
