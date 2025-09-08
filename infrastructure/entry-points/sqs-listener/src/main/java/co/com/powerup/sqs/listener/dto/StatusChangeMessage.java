package co.com.powerup.sqs.listener.dto;

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
public class StatusChangeMessage {
    private Long requestId;
    private Long statusId;
    private String decision;
}
