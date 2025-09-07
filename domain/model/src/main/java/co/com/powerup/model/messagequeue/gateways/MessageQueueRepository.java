package co.com.powerup.model.messagequeue.gateways;

import java.util.Map;

import reactor.core.publisher.Mono;

public interface MessageQueueRepository {
    Mono<Void> sendMessageChangeStatus(Map<String, Object> messageMap);
}
