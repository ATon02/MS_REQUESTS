package co.com.powerup.api.requesttype;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.dtos.request.RequestTypeCreateDTO;
import co.com.powerup.api.mapper.RequestTypeDTOMapper;
import co.com.powerup.usecase.requesttype.IRequestTypeUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RequestTypeHandler {
    
    private  final RequestTypeDTOMapper  requestTypeDTOMapper;
    private final IRequestTypeUseCase requestTypeUseCase;

    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        System.out.println("➡️ Entró al handler find() de RequestTypeHandler");
        return requestTypeUseCase.findAll()
                .map(requestTypeDTOMapper::toResponse)
                .collectList()
                .flatMap(types -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(types))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage())));

    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        System.out.println("➡️ Entró al handler save() de RequestTypeHandler");
        return serverRequest.bodyToMono(RequestTypeCreateDTO.class)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
            .map(requestTypeDTOMapper::toModel)              
            .flatMap(requestTypeUseCase::saveType)  
            .map(requestTypeDTOMapper::toResponse)         
            .flatMap(requestType -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestType))
            .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage())));  
    }
}
