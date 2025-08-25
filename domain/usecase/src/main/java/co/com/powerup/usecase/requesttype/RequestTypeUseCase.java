package co.com.powerup.usecase.requesttype;

import co.com.powerup.model.requesttype.RequestType;
import co.com.powerup.model.requesttype.gateways.RequestTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RequestTypeUseCase implements IRequestTypeUseCase {

    private final RequestTypeRepository requestTypeRepository;

    @Override
    public Mono<RequestType> saveType(RequestType requestType) {
        System.out.println("➡️ Entró al handler saveType() de RequestTypeUseCase");
        if (requestType.getName() == null || requestType.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException("El campo 'name' es obligatorio."));
        }

        if (requestType.getMinAmount() == null || requestType.getMinAmount() <= 0) {
            return Mono.error(new IllegalArgumentException("El campo 'minAmount' es obligatorio y debe ser mayor a 0."));
        }

        if (requestType.getMaxAmount() == null || requestType.getMaxAmount() <= 0) {
            return Mono.error(new IllegalArgumentException("El campo 'maxAmount' es obligatorio y debe ser mayor a 0."));
        }

        if (requestType.getInterestRate() == null || requestType.getInterestRate() <= 0) {
            return Mono.error(new IllegalArgumentException("El campo 'interestRate' es obligatorio y debe ser mayor a 0."));
        }

        if (requestType.getAutomaticValidation() == null) {
            return Mono.error(new IllegalArgumentException("El campo 'automaticValidation' es obligatorio."));
        }

        return requestTypeRepository.save(requestType);
    }


    @Override
    public Flux<RequestType> findAll() {
        return requestTypeRepository.findAll();
    }
}
