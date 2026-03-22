package construccion2.demo.domain.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
// IMPORTANTE: "extends Person" aplica la herencia (Diagrama flecha blanca)
public class User extends Person {
    
    private String username; // Aplica la Regla R-022
    private String password; // Aplica la Regla R-022
    
    private Role role;
    private UserStatus status;
    
    private String relatedId; // Aplica la Regla R-026
}