package co.com.powerup.sqs.sender;

import co.com.powerup.model.messagequeue.gateways.MessageQueueRepository;
import co.com.powerup.sqs.sender.calculatedebtcapacity.SQSSenderPropertiesCalculateDebtCapacity;
import co.com.powerup.sqs.sender.statuschange.SQSSenderPropertiesStatusChange;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements MessageQueueRepository /*implements SomeGateway*/ {
    private final SQSSenderPropertiesStatusChange propertiesStatusChange;
    private final SQSSenderPropertiesCalculateDebtCapacity propertiesCalculateDebtCapacity;
    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> sendMessageChangeStatus(Map<String, Object> messageMap) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(messageMap))
                .flatMap(json -> Mono.fromFuture(client.sendMessage(
                        SendMessageRequest.builder()
                                .queueUrl(propertiesStatusChange.queueUrl())
                                .messageBody(json)
                                .build())))
                .doOnNext(response -> log.info("Message sent SQS requests-notification, id: {}", response.messageId()))
                .then();
    }

    @Override
    public Mono<Void> sendMessageCalculateDebtCapacity(Map<String, Object> messageMap) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(messageMap))
                .flatMap(json -> Mono.fromFuture(client.sendMessage(
                        SendMessageRequest.builder()
                                .queueUrl(propertiesCalculateDebtCapacity.queueUrl())
                                .messageBody(json)
                                .build())))
                .doOnNext(response -> log.info("Message sent SQS calculate-capacity, id: {}", response.messageId()))
                .then();
    }

}
