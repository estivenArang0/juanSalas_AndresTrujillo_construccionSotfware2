package construccion2.demo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Loan extends BankProduct {
    private long id;
    private LoanType loanType;
    private String clientId;
    
    // Lo que el cliente pide
    private double requestedAmount; 
    // Aplica la Regla R-040
    // Nota: Se usa Double (wrapper) en vez de double (primitivo) para permitir null cuando el préstamo está "En estudio".
    private Double approvedAmount;
    
    private double interestRate;
    private int loanMonths;
    
    // Aplica la Regla R-024.
    private LoanStatus status; 
    
    private Date approvalDate;
    private Date disbursementDate;
    private String disbursementAccount; // Aplica la Regla R-010.
}