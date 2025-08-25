package co.com.powerup.usecase.requeststatus;

import co.com.powerup.model.requeststatus.RequestStatus;
import co.com.powerup.model.requeststatus.gateways.RequestStatusRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor

public class RequestStatusUseCase implements IRequestStatusUseCase {

    private final RequestStatusRepository requestStatusRepository;

    @Override
    public Mono<RequestStatus> saveStatus(RequestStatus requestStatus) {
        System.out.println("➡️ Entró al handler saveStatus() de RequestStatusUseCase");
        if (requestStatus.getName() == null || requestStatus.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException("El campo 'name' es obligatorio."));
        }
        if (requestStatus.getDescription() == null || requestStatus.getDescription().isBlank()) {
            return Mono.error(new IllegalArgumentException("El campo 'description' es obligatorio."));
        }

        return requestStatusRepository.save(requestStatus);
    }

    @Override
    public Flux<RequestStatus> findAll() {
        return requestStatusRepository.findAll();
    }
}
