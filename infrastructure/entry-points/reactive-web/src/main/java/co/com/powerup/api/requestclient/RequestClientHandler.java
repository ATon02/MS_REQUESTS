package co.com.powerup.api.requestclient;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.dtos.request.RequestClientCreateDTO;
import co.com.powerup.api.mapper.RequestClientDTOMapper;
import co.com.powerup.usecase.requestclient.IRequestClientUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RequestClientHandler {

    private  final RequestClientDTOMapper  requestClientDTOMapper;
    private final IRequestClientUseCase requestClientUseCase;

    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        System.out.println("➡️ Entró al handler find() de RequestClientHandler");
        return requestClientUseCase.findAll()
                .map(requestClientDTOMapper::toResponse)
                .collectList()
                .flatMap(requests -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requests))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage())));

    }
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        System.out.println("➡️ Entró al handler save() de RequestClientHandler");
        return serverRequest.bodyToMono(RequestClientCreateDTO.class)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
            .map(requestClientDTOMapper::toModel)              
            .flatMap(requestClientUseCase::saveRequest)  
            .map(requestClientDTOMapper::toResponse)         
            .flatMap(requestClient -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestClient))
            .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage())));  
    }
}
