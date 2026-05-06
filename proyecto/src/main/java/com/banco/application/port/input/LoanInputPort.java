package com.bank.app.application.port.input;
import com.bank.app.application.dto.request.CreateLoanRequest;
import com.bank.app.application.dto.request.ApproveLoanRequest;
import com.bank.app.application.dto.request.DisburseLoanRequest;
import com.bank.app.application.dto.response.LoanResponse;
import java.util.List;
public interface LoanInputPort {
    LoanResponse requestLoan(CreateLoanRequest request);
    LoanResponse approveLoan(Long loanId, ApproveLoanRequest request, Long analystId);
    LoanResponse rejectLoan(Long loanId, Long analystId);
    LoanResponse disburseLoan(Long loanId, DisburseLoanRequest request, Long analystId);
    LoanResponse getLoanById(Long loanId);
    List<LoanResponse> getLoansByClient(String clientId);
}
