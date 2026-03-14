package construccion2.demo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankProduct {
    private String productCode;
    private String productName;
    private ProductCategory category;
    private boolean requiresApproval;
}