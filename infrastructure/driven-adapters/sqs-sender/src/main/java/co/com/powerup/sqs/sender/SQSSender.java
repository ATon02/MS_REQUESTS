package co.com.powerup.sqs.sender;

import co.com.powerup.model.messagequeue.gateways.MessageQueueRepository;
import co.com.powerup.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements MessageQueueRepository /*implements SomeGateway*/ {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper;

    public Mono<String> send(String message) {
        return Mono.fromCallable(() -> buildRequest(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<Void> sendMessageChangeStatus(Map<String, Object> messageMap) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(messageMap))
                .flatMap(json -> Mono.fromFuture(client.sendMessage(
                        SendMessageRequest.builder()
                                .queueUrl(properties.queueUrl())
                                .messageBody(json)
                                .build())))
                .doOnNext(response -> log.info("Message sent {}", response.messageId()))
                .then();
    }

}
