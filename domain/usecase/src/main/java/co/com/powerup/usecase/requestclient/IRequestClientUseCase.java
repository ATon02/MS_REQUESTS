package co.com.powerup.usecase.requestclient;

import java.util.List;

import co.com.powerup.model.requestclient.RequestClient;
import co.com.powerup.usecase.requestclient.dto.ResponseDataRequest;
import co.com.powerup.usecase.requestclient.dto.ResponseDataTotal;
import co.com.powerup.usecase.requestclient.enums.TypeTotal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRequestClientUseCase {

    Mono<RequestClient> saveRequest(RequestClient requestClient, String authorization);
    Flux<RequestClient> findAll();
    Flux<ResponseDataRequest> findByFilter(List<Long> status, Integer page, Integer size, String authorization );
    Mono<RequestClient> updateStatus(Long requestId, Long statusId);
    Mono<RequestClient> updateStatusListener(Long requestId, Long statusId);
    Mono<ResponseDataTotal> totalByStatus(TypeTotal type, Long statusId);

}
