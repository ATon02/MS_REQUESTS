package co.com.powerup.usecase.requestclient;

import java.util.List;
import java.util.Optional;

import co.com.powerup.model.requestclient.RequestClient;
import co.com.powerup.model.requestclient.gateways.RequestClientRepository;
import co.com.powerup.model.requeststatus.RequestStatus;
import co.com.powerup.model.requeststatus.gateways.RequestStatusRepository;
import co.com.powerup.model.requesttype.RequestType;
import co.com.powerup.model.requesttype.gateways.RequestTypeRepository;
import co.com.powerup.model.userinfo.UserInfo;
import co.com.powerup.model.userinfo.gateways.UserInfoRepository;
import co.com.powerup.usecase.requestclient.dto.ResponseDataRequest;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RequestClientUseCase implements IRequestClientUseCase {

    private final RequestClientRepository requestClientRepository;
    private final RequestTypeRepository requestTypeRepository;
    private final UserInfoRepository userInfoRepository;
    private final RequestStatusRepository requestStatusRepository;

    private static final List<Long> ESTADOS_ASESOR = List.of(1L, 3L, 5L);



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

    @Override
    public Flux<ResponseDataRequest> findByFilter(List<Long> statusIds, Integer page, Integer size, String authorization) {
        if (page == null || size == null || page <= 0 || size <= 0) {
            return Flux.error(new IllegalArgumentException("Los parámetros de página y tamaño deben ser mayores a 0."));
        }
        int offset = (page - 1) * size;
        if (!ESTADOS_ASESOR.containsAll(statusIds)) {
            return Flux.error(new IllegalArgumentException("Solo se pueden filtrar los estados permitidos para el asesor (1)Pendiente por revisión,(3)Rechazada,(5)Revision manual"));
        }
        return requestClientRepository.findByStatusIds((statusIds == null || statusIds.isEmpty()) ? ESTADOS_ASESOR : statusIds, offset, size)
            .flatMap(requestClient -> buildResponseDataRequest(requestClient, authorization));
    }

    private Mono<ResponseDataRequest> buildResponseDataRequest(RequestClient requestClient,String authorization) {
        Mono<RequestType> requestTypeMono = requestTypeRepository.findById(requestClient.getRequestTypeId())
            .switchIfEmpty(Mono.error(
                new IllegalArgumentException("Tipo de la solicitud " + requestClient.getId() + " no encontrado")
            ));
        Mono<RequestStatus> requestStatusMono = requestStatusRepository.findById(requestClient.getStatusId())
            .switchIfEmpty(Mono.error(
                new IllegalArgumentException("Estado de solicitud " + requestClient.getId() + " no encontrado")
            ));
        Mono<UserInfo> userInfoMono = userInfoRepository.findByEmail(requestClient.getEmail(), authorization)
            .switchIfEmpty(Mono.error(
                new IllegalArgumentException("Información del usuario con email " + requestClient.getEmail() + " no encontrada")
            ));
        return Mono.zip(requestTypeMono, requestStatusMono, userInfoMono)
            .map(tuple -> {
                RequestType requestType = tuple.getT1();
                RequestStatus requestStatus = tuple.getT2();
                UserInfo userInfo = tuple.getT3();
                return ResponseDataRequest.builder()
                    .id(requestClient.getId())
                    .amount(requestClient.getAmount())
                    .deadline(requestClient.getDeadline())
                    .email(requestClient.getEmail())
                    .name(Optional.ofNullable(userInfo.getName()).orElse("Nombre no disponible"))
                    .baseSalary(Optional.ofNullable(userInfo.getBaseSalary()).orElse(0.0))
                    .totalMonthlyDebt((requestClient.getAmount()*(requestType.getInterestRate()*100))/requestClient.getDeadline())
                    .interestRate(Optional.ofNullable(requestType.getInterestRate()).orElse(0.0))
                    .requestType(requestType.getName())
                    .requestStatus(requestStatus.getName())
                    .build();
            });
    }



}
