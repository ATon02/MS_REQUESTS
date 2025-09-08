package co.com.powerup.sqs.sender.statuschange;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs.statuschange")
public record SQSSenderPropertiesStatusChange(
     String region,
     String queueUrl,
     String endpoint){
}
