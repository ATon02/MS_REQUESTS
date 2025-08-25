package co.com.powerup.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "request_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestStatusEntity {

    @Id
    private Long id;
    private String name;
    private String description;

}
