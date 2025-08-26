package co.com.powerup.api.requeststatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.dtos.request.RequestStatusCreateDTO;
import co.com.powerup.api.mapper.RequestStatusDTOMapper;
import co.com.powerup.usecase.requeststatus.IRequestStatusUseCase;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestStatusHandler {
    private  final RequestStatusDTOMapper  requestStatusDTOMapper;
    private final IRequestStatusUseCase requestStatusUseCase;

    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        log.info("➡️ Entró al handler find() de RequestStatusHandler");
        return requestStatusUseCase.findAll()
                .map(requestStatusDTOMapper::toResponse)
                .collectList()
                .flatMap(states -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(states));
    }
    
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        log.info("➡️ Entró al handler save() de RequestStatusHandler");
        return serverRequest.bodyToMono(RequestStatusCreateDTO.class)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
            .map(requestStatusDTOMapper::toModel)              
            .flatMap(requestStatusUseCase::saveStatus)  
            .map(requestStatusDTOMapper::toResponse)         
            .flatMap(requestStatus -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestStatus));  
    }
}
