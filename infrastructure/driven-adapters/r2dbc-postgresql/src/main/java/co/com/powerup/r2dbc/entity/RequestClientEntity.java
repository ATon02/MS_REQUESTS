package co.com.powerup.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "request_client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestClientEntity {

    @Id
    private Long id;
    private Double amount;
    private Long deadline;
    private String email;
    private Long requestTypeId;
    private Long statusId;
    private String identityDocument;

}
