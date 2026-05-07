package com.banco.application.usecase;

import com.banco.application.dto.request.CreateBankAccountRequest;
import com.banco.application.dto.response.BankAccountResponse;
import com.banco.application.port.input.BankAccountInputPort;
import com.banco.domain.exception.ResourceNotFoundException;
import com.banco.domain.exception.UserNotActiveException;
import com.banco.domain.model.entity.BankAccount;
import com.banco.domain.model.entity.User;
import com.banco.domain.model.valueobject.AccountStatus;
import com.banco.domain.model.valueobject.Money;
import com.banco.domain.repository.BankAccountRepository;
import com.banco.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountUseCase implements BankAccountInputPort {

    private final BankAccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BankAccountResponse openAccount(CreateBankAccountRequest req) {
        User owner = userRepository.findByIdentificationNumber(req.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found: " + req.getOwnerId()));

        if (!owner.isActive())
            throw new UserNotActiveException("Cannot open account for inactive/blocked client");

        String accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();

        BankAccount account = BankAccount.builder()
                .accountNumber(accountNumber)
                .accountType(req.getAccountType())
                .ownerId(req.getOwnerId())
                .balance(Money.zero(req.getCurrency()))
                .status(AccountStatus.ACTIVE)
                .openingDate(LocalDate.now())
                .build();

        BankAccount saved = accountRepository.save(account);
        return toResponse(saved, req.getCurrency());
    }

    @Override
    public BankAccountResponse getAccountByNumber(String accountNumber) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));
        return toResponse(account, account.getBalance().getCurrency());
    }

    @Override
    public List<BankAccountResponse> getAccountsByOwner(String ownerId) {
        return accountRepository.findByOwnerId(ownerId).stream()
                .map(a -> toResponse(a, a.getBalance().getCurrency()))
                .collect(Collectors.toList());
    }

    private BankAccountResponse toResponse(BankAccount a, String currency) {
        return BankAccountResponse.builder()
                .id(a.getId())
                .accountNumber(a.getAccountNumber())
                .accountType(a.getAccountType())
                .ownerId(a.getOwnerId())
                .balance(a.getBalance().getAmount())
                .currency(currency)
                .status(a.getStatus())
                .openingDate(a.getOpeningDate())
                .build();
    }
}
