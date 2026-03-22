package construccion2.demo.domain.models;
    
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private long id;
    private String name;
    private String idNumber;    // Aplica la Regla R-001
    private String email;       // Aplica la Regla R-003
    private String phone;       // Aplica la Regla R-003
    private String address;
    private Date birthDate;
}
