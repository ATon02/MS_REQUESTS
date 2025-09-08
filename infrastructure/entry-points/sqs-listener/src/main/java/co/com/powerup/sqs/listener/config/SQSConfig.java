package co.com.powerup.sqs.listener.config;

import co.com.powerup.sqs.listener.SQSProcessor;
import co.com.powerup.sqs.listener.helper.SQSListener;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@RequiredArgsConstructor
public class SQSConfig {

    private final SqsAsyncClient client;
    private final SQSProperties properties;
    private final SQSProcessor processor;

    @Bean
    public SQSListener sqsListener() {
        return SQSListener.builder()
                .client(client)
                .properties(properties)
                .processor(processor)
                .build();
    }
}
