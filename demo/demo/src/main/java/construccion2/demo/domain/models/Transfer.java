package construccion2.demo.domain.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {
    private long id;
    private String sourceAccount;
    private String destinationAccount;
    private double amount;
    
    // Aplica la Regla R-014.
    private Timestamp creationDate;
    
    private Timestamp approvalDate;
    private TransferStatus status;
    private long creatorUserId;
    private long approverUserId;
    
    // Asociación con Loan
    private Loan loan;
}