package co.com.powerup.model.requestclient.gateways;

import co.com.powerup.model.requestclient.RequestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RequestClientRepository {

    Mono<RequestClient> save(RequestClient requestType); 
    Flux<RequestClient> findAll();

}
