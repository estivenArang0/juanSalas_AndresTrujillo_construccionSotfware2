package construccion2.demo.domain.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.sql.Timestamp;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    private String id; // Es UUID string para MongoDB
    private OperationType operationType;
    private Timestamp operationDateTime;
    private long userId;
    private String userRole;
    private String affectedProductId; // Aplica la Regla R-038.
    
    // Map para MongoDb: Permite guardar flexibilidad técnica.
    // Ejemplo: Guardar { "balance_anterior": 500, "balance_nuevo": 200 }
    private Map<String, Object> details;
}