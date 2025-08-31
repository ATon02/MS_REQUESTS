package co.com.powerup.model.requeststatus.gateways;


import co.com.powerup.model.requeststatus.RequestStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RequestStatusRepository {

    Mono<RequestStatus> save(RequestStatus RequestStatus); 
    Mono<RequestStatus> findById(Long id); 
    Flux<RequestStatus> findAll();
}
