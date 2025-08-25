package co.com.powerup.usecase.requestclient;

import co.com.powerup.model.requestclient.RequestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRequestClientUseCase {

    Mono<RequestClient> saveRequest(RequestClient requestClient);
    Flux<RequestClient> findAll();

}
