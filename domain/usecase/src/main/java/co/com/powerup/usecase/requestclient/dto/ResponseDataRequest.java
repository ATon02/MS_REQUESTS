package co.com.powerup.usecase.requestclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ResponseDataRequest {
    private Long id;
    private Double amount;
    private Long deadline;
    private String email;
    private String name; 
    private String requestType;
    private String requestStatus;
    private Double baseSalary; 
    private Double totalMonthlyDebt;
    private Double interestRate;

}
