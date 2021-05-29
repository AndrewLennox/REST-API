import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Agents implements Serializable {

   
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)  
    private Long agent_Id;
    private String name;
    private String phone;
    private String fax;
    private String email;
    private String username;
    private String password;
   
}

