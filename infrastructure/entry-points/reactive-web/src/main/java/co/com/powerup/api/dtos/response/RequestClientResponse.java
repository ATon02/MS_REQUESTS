package co.com.powerup.api.dtos.response;

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
public class RequestClientResponse {
    private Long id;
    private Double amount;
    private Long deadline;
    private String email;
    private Long requestTypeId;
    private Long statusId;
}
