package com.bank.app.application.port.input;
import com.bank.app.application.dto.request.CreateBankAccountRequest;
import com.bank.app.application.dto.response.BankAccountResponse;
import java.util.List;
public interface BankAccountInputPort {
    BankAccountResponse openAccount(CreateBankAccountRequest request);
    BankAccountResponse getAccountByNumber(String accountNumber);
    List<BankAccountResponse> getAccountsByOwner(String ownerId);
}
