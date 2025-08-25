package co.com.powerup.usecase.requestclient;

import co.com.powerup.model.requestclient.RequestClient;
import co.com.powerup.model.requestclient.gateways.RequestClientRepository;
import co.com.powerup.model.requesttype.gateways.RequestTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RequestClientUseCase implements IRequestClientUseCase {

    private final RequestClientRepository requestClientRepository;
    private final RequestTypeRepository requestTypeRepository;


    @Override
    public Mono<RequestClient> saveRequest(RequestClient requestClient) {
         System.out.println("➡️ Entró al handler saveRequest() de RequestClientUseCase");
        
         if (requestClient.getRequestTypeId() == null) {
            return Mono.error(new IllegalArgumentException("El tipo de préstamo es obligatorio."));
        }

        if (requestClient.getIdentityDocument() == null || requestClient.getIdentityDocument().isBlank()) {
            return Mono.error(new IllegalArgumentException("El documento de identidad del cliente es obligatorio."));
        }

        if (requestClient.getEmail() == null || requestClient.getEmail().isBlank()) {
            return Mono.error(new IllegalArgumentException("El email es obligatorio."));
        }

        if (!requestClient.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            return Mono.error(new IllegalArgumentException("El email tiene un formato inválido."));
        }

        if (requestClient.getAmount() == null || requestClient.getAmount() <= 0) {
            return Mono.error(new IllegalArgumentException("Monto no válido, debe ser mayor a 0."));
        }

        if (requestClient.getDeadline() == null || requestClient.getDeadline() <= 0) {
            return Mono.error(new IllegalArgumentException("El plazo debe ser mayor a 0."));
        }

        if (!requestClient.getIdentityDocument().matches("\\d+")) {
            return Mono.error(new IllegalArgumentException("El documento de identidad solo debe contener números."));
        }


        return requestTypeRepository.findById(requestClient.getRequestTypeId())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El tipo de préstamo no existe.")))
            .flatMap(requestType -> {
                requestClient.setStatusId(1L);
                return requestClientRepository.save(requestClient);
            });
    }



    @Override
    public Flux<RequestClient> findAll() {
        return requestClientRepository.findAll();
    }
}
