package co.com.powerup.model.requesttype;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestType {
    private Long id;
    private String name;
    private Double minAmount;
    private Double maxAmount;
    private Double interestRate; 
    private Boolean automaticValidation;
}
