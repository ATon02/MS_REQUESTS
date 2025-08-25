package co.com.powerup.usecase.requeststatus;

import co.com.powerup.model.requeststatus.RequestStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRequestStatusUseCase {

    Mono<RequestStatus> saveStatus(RequestStatus requestStatus);
    Flux<RequestStatus> findAll();

}
