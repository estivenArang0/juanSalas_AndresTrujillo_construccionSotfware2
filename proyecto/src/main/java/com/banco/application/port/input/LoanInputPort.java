package com.banco.application.port.input;
import com.banco.application.dto.request.CreateLoanRequest;
import com.banco.application.dto.request.ApproveLoanRequest;
import com.banco.application.dto.request.DisburseLoanRequest;
import com.banco.application.dto.response.LoanResponse;
import java.util.List;
public interface LoanInputPort {
    LoanResponse requestLoan(CreateLoanRequest request);
    LoanResponse approveLoan(Long loanId, ApproveLoanRequest request, Long analystId);
    LoanResponse rejectLoan(Long loanId, Long analystId);
    LoanResponse disburseLoan(Long loanId, DisburseLoanRequest request, Long analystId);
    LoanResponse getLoanById(Long loanId);
    List<LoanResponse> getLoansByClient(String clientId);
}
