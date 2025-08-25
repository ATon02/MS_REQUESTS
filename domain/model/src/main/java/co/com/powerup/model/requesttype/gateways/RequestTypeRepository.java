package co.com.powerup.model.requesttype.gateways;

import co.com.powerup.model.requesttype.RequestType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RequestTypeRepository {
    
    Mono<RequestType> save(RequestType requestType);
    Flux<RequestType> findAll();
    Mono<RequestType> findById(Long id);

}
