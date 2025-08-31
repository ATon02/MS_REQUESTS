package co.com.powerup.api.requestclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.dtos.request.RequestClientCreateDTO;
import co.com.powerup.api.exceptions.ForbiddenException;
import co.com.powerup.api.mapper.RequestClientDTOMapper;
import co.com.powerup.usecase.requestclient.IRequestClientUseCase;
import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestClientHandler {

    private  final RequestClientDTOMapper  requestClientDTOMapper;
    private final IRequestClientUseCase requestClientUseCase;

    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        log.info("➡️ Entró al handler find() de RequestClientHandler");
        return requestClientUseCase.findAll()
                .map(requestClientDTOMapper::toResponse)
                .collectList()
                .flatMap(requests -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requests));

    }
    @SuppressWarnings("null")
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        log.info("➡️ Entró al handler save() de RequestClientHandler");
        Claims claims = (Claims) serverRequest.exchange().getAttribute("claims");
        String emailSub = claims.getSubject();
        return serverRequest.bodyToMono(RequestClientCreateDTO.class)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
            .map(requestClientDTOMapper::toModel)
            .flatMap(user -> {
                if (!user.getEmail().equals(emailSub)) {
                    return Mono.error(new ForbiddenException("El email de la solicitud no coincide con el del solicitante"));
                }
                return requestClientUseCase.saveRequest(user);
            })              
            .map(requestClientDTOMapper::toResponse)         
            .flatMap(requestClient -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestClient));  
    }

    public Mono<ServerResponse> findByFilter(ServerRequest serverRequest) {
        log.info("➡️ Entró al handler findByFilter() de RequestClientHandler");

        Integer page = Integer.parseInt(serverRequest.queryParam("page").orElse("1"));
        Integer size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));
        List<Long> statusIds = serverRequest.queryParam("status").map(s -> Arrays.stream(s.split(","))
                        .map(Long::parseLong).collect(Collectors.toList())).orElse(Collections.emptyList());
        String token = serverRequest.headers()
                                .firstHeader("Authorization");
        return requestClientUseCase.findByFilter(statusIds, page, size, token)
                .collectList()
                .flatMap(requests -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requests));
    }

}
