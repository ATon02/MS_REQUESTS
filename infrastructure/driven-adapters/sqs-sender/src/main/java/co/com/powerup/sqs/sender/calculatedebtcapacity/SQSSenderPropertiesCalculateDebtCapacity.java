package co.com.powerup.sqs.sender.calculatedebtcapacity;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs.calculatedebtcapacity")
public record SQSSenderPropertiesCalculateDebtCapacity(
     String region,
     String queueUrl,
     String endpoint){
}
