package co.com.powerup.sqs.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import co.com.powerup.sqs.listener.dto.StatusChangeMessage;
import co.com.powerup.usecase.requestclient.IRequestClientUseCase;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    
    private final ObjectMapper objectMapper;
    private final IRequestClientUseCase requestClientUseCase;

    @Override
    public Mono<Void> apply(Message message) {
        log.info("Mensaje recibido desde SQS: {}", message.body());
        return Mono.fromCallable(() -> objectMapper.readValue(message.body(), StatusChangeMessage.class))
            .onErrorResume(e -> {
                log.error("Error parseando mensaje SQS: {}", message.body(), e);
                return Mono.empty(); 
            })
            .flatMap(dto -> 
                requestClientUseCase.updateStatus(dto.getRequestId(), dto.getStatusId())
                    .doOnSuccess(v -> log.info("Estado actualizado para requestId={} con decisión={}", 
                                                dto.getRequestId(), dto.getDecision()))
                    .onErrorResume(IllegalArgumentException.class, e -> {
                        log.warn("Solicitud {} ya estaba en estado {}, se ignora. Msg: {}", 
                                dto.getRequestId(), dto.getStatusId(), e.getMessage());
                        return Mono.empty();
                    })
            )
            .then();
    }

}
