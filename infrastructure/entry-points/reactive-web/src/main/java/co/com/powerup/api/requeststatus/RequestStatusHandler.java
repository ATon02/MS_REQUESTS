package co.com.powerup.api.requeststatus;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.dtos.request.RequestStatusCreateDTO;
import co.com.powerup.api.mapper.RequestStatusDTOMapper;
import co.com.powerup.usecase.requeststatus.IRequestStatusUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RequestStatusHandler {
    private  final RequestStatusDTOMapper  requestStatusDTOMapper;
    private final IRequestStatusUseCase requestStatusUseCase;

    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        System.out.println("➡️ Entró al handler find() de RequestStatusHandler");
        return requestStatusUseCase.findAll()
                .map(requestStatusDTOMapper::toResponse)
                .collectList()
                .flatMap(states -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(states))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage())));
    }
    
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        System.out.println("➡️ Entró al handler save() de RequestStatusHandler");
        return serverRequest.bodyToMono(RequestStatusCreateDTO.class)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
            .map(requestStatusDTOMapper::toModel)              
            .flatMap(requestStatusUseCase::saveStatus)  
            .map(requestStatusDTOMapper::toResponse)         
            .flatMap(requestStatus -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestStatus))
            .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage())));  
    }
}
