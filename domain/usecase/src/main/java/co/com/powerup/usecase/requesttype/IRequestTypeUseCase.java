package co.com.powerup.usecase.requesttype;

import co.com.powerup.model.requesttype.RequestType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRequestTypeUseCase {

    Mono<RequestType> saveType(RequestType requestType);
    Flux<RequestType> findAll();

}
