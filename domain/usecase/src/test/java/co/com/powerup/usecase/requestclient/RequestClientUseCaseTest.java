package co.com.powerup.usecase.requestclient;

import co.com.powerup.model.messagequeue.gateways.MessageQueueRepository;
import co.com.powerup.model.requestclient.RequestClient;
import co.com.powerup.model.requestclient.gateways.RequestClientRepository;
import co.com.powerup.model.requeststatus.RequestStatus;
import co.com.powerup.model.requeststatus.gateways.RequestStatusRepository;
import co.com.powerup.model.requesttype.RequestType;
import co.com.powerup.model.requesttype.gateways.RequestTypeRepository;
import co.com.powerup.model.userinfo.UserInfo;
import co.com.powerup.model.userinfo.gateways.UserInfoRepository;
import co.com.powerup.usecase.requestclient.dto.ResponseDataRequest;
import co.com.powerup.usecase.requestclient.dto.ResponseDataTotal;
import co.com.powerup.usecase.requestclient.enums.TypeTotal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class RequestClientUseCaseTest {

    @Mock
    private RequestClientRepository requestClientRepository;

    @Mock
    private RequestTypeRepository requestTypeRepository;

    @Mock
    private RequestStatusRepository requestStatusRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private MessageQueueRepository messageQueueRepository;

    @InjectMocks
    private RequestClientUseCase requestClientUseCase;

    private RequestClient validRequest;

    private String auth;

    @BeforeEach
    void setUp() {
        auth = "toke jwt";
        validRequest = new RequestClient();
        validRequest.setRequestTypeId(1L);
        validRequest.setIdentityDocument("123456789");
        validRequest.setEmail("cliente@example.com");
        validRequest.setAmount(1000.0);
        validRequest.setDeadline(12L);
    }

    @Test
    void saveRequest_whenValid_shouldSaveSuccessfully() {
        RequestType stubRequestType = new RequestType();
        stubRequestType.setId(1L);
        stubRequestType.setName("Préstamo Personal");

        when(requestTypeRepository.findById(1L)).thenReturn(Mono.just(stubRequestType));
        when(requestClientRepository.save(any(RequestClient.class))).thenReturn(Mono.just(validRequest));

        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectNextMatches(req -> req.getEmail().equals("cliente@example.com") &&
                        req.getStatusId() == 1L)
                .verifyComplete();

        verify(requestTypeRepository, times(1)).findById(1L);
        verify(requestClientRepository, times(1)).save(validRequest);
    }

    @Test
    void saveRequest_whenRequestTypeIdNull_shouldReturnError() {
        validRequest.setRequestTypeId(null);

        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El tipo de préstamo es obligatorio."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenIdentityDocumentNullOrBlank_shouldReturnError() {
        validRequest.setIdentityDocument(null);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El documento de identidad del cliente es obligatorio."))
                .verify();

        validRequest.setIdentityDocument("");
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El documento de identidad del cliente es obligatorio."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenEmailNullOrBlank_shouldReturnError() {
        validRequest.setEmail(null);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El email es obligatorio."))
                .verify();

        validRequest.setEmail("");
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El email es obligatorio."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenEmailInvalid_shouldReturnError() {
        validRequest.setEmail("correo-invalido");

        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El email tiene un formato inválido."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenAmountInvalid_shouldReturnError() {
        validRequest.setAmount(null);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("Monto no válido, debe ser mayor a 0."))
                .verify();

        validRequest.setAmount(0.0);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("Monto no válido, debe ser mayor a 0."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenDeadlineInvalid_shouldReturnError() {
        validRequest.setDeadline(null);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El plazo debe ser mayor a 0."))
                .verify();

        validRequest.setDeadline(0L);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El plazo debe ser mayor a 0."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenIdentityDocumentInvalid_shouldReturnError() {
        validRequest.setIdentityDocument("ABC123");

        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El documento de identidad solo debe contener números."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenRequestTypeDoesNotExist_shouldReturnError() {
        when(requestTypeRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(requestClientUseCase.saveRequest(validRequest,auth))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El tipo de préstamo no existe."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void findAll_shouldReturnRequests() {
        when(requestClientRepository.findAll()).thenReturn(Flux.just(validRequest));

        StepVerifier.create(requestClientUseCase.findAll())
                .expectNext(validRequest)
                .verifyComplete();

        verify(requestClientRepository, times(1)).findAll();
    }

    @Test
    void testFindByFilter_success() {
        String token = "Bearer test-token";

        RequestClient requestClient = new RequestClient();
        requestClient.setId(1L);
        requestClient.setAmount(1000.0);
        requestClient.setDeadline(12L);
        requestClient.setEmail("test@example.com");
        requestClient.setRequestTypeId(1L);
        requestClient.setStatusId(1L);
        RequestType requestType = new RequestType();
        requestType.setId(1L);
        requestType.setName("Préstamo personal");
        requestType.setInterestRate(0.12);
        RequestStatus requestStatus = new RequestStatus();
        requestStatus.setId(1L);
        requestStatus.setName("Pendiente");
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@example.com");
        userInfo.setName("Juan Pérez");
        userInfo.setBaseSalary(2000.0);
        when(requestClientRepository.findByStatusIds(any(), anyInt(), anyInt()))
                .thenReturn(Flux.just(requestClient));
        when(requestTypeRepository.findById(1L)).thenReturn(Mono.just(requestType));
        when(requestStatusRepository.findById(1L)).thenReturn(Mono.just(requestStatus));
        when(userInfoRepository.findByEmail("test@example.com", token)).thenReturn(Mono.just(userInfo));
        Flux<ResponseDataRequest> result = requestClientUseCase.findByFilter(List.of(1L), 1, 10, token);
        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.getId().equals(1L) &&
                        resp.getName().equals("Juan Pérez") &&
                        resp.getTotalMonthlyDebt() > 0)
                .verifyComplete();
    }

    @Test
    void testFindByFilter_invalidPageOrSize() {
        StepVerifier.create(requestClientUseCase.findByFilter(List.of(1L), 0, 10, "token"))
                .expectError(IllegalArgumentException.class)
                .verify();

        StepVerifier.create(requestClientUseCase.findByFilter(List.of(1L), 1, 0, "token"))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void testFindByFilter_invalidPageOrSizeNull() {
        StepVerifier.create(requestClientUseCase.findByFilter(List.of(1L), null, 10, "token"))
                .expectError(IllegalArgumentException.class)
                .verify();

        StepVerifier.create(requestClientUseCase.findByFilter(List.of(1L), 1, null, "token"))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void testFindByFilter_invalidListIds() {
        StepVerifier.create(requestClientUseCase.findByFilter(List.of(1L,2L), 1, 10, "token"))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void testBuildResponseDataRequest_userNotFound() {
        String token = "Bearer test-token";

        RequestClient requestClient = new RequestClient();
        requestClient.setId(1L);
        requestClient.setAmount(1000.0);
        requestClient.setDeadline(12L);
        requestClient.setEmail("notfound@example.com");
        requestClient.setRequestTypeId(1L);
        requestClient.setStatusId(1L);

        RequestType requestType = new RequestType();
        requestType.setId(1L);
        requestType.setInterestRate(0.12);
        requestType.setName("Préstamo personal");

        RequestStatus requestStatus = new RequestStatus();
        requestStatus.setId(1L);
        requestStatus.setName("Pendiente");

        when(requestClientRepository.findByStatusIds(any(), anyInt(), anyInt()))
            .thenReturn(Flux.just(requestClient));
        when(requestTypeRepository.findById(1L)).thenReturn(Mono.just(requestType));
        when(requestStatusRepository.findById(1L)).thenReturn(Mono.just(requestStatus));
        when(userInfoRepository.findByEmail("notfound@example.com", token))
                .thenReturn(Mono.empty());

        StepVerifier.create(requestClientUseCase.findByFilter(List.of(1L), 1, 10, token))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("Información del usuario"))
                .verify();
    }

    @SuppressWarnings("unchecked")
    @Test
    void updateStatus_Success() {
        Long requestId = 1L;
        Long statusId = 2L;

        RequestClient client = new RequestClient();
        client.setId(requestId);
        client.setStatusId(1L);
        client.setEmail("test@email.com");

        RequestStatus status = new RequestStatus();
        status.setId(statusId);
        status.setName("Aprobada");

        when(requestClientRepository.findById(requestId)).thenReturn(Mono.just(client));
        when(requestStatusRepository.findById(statusId)).thenReturn(Mono.just(status));
        when(requestClientRepository.save(any(RequestClient.class))).thenReturn(Mono.just(client));
        when(messageQueueRepository.sendMessageChangeStatus(any(Map.class))).thenReturn(Mono.empty());

        Mono<RequestClient> result = requestClientUseCase.updateStatus(requestId, statusId);

        StepVerifier.create(result)
                .expectNextMatches(savedClient -> savedClient.getStatusId().equals(statusId))
                .verifyComplete();

        verify(messageQueueRepository, times(1)).sendMessageChangeStatus(any(Map.class));
    }

    @Test
    void updateStatus_RequestNotFound() {
        Long requestId = 1L;
        Long statusId = 2L;

        when(requestClientRepository.findById(requestId)).thenReturn(Mono.empty());

        Mono<RequestClient> result = requestClientUseCase.updateStatus(requestId, statusId);

        StepVerifier.create(result)
                .expectErrorMessage("Solicitud con id " + requestId + " no encontrada")
                .verify();
    }

    @Test
    void updateStatus_StatusNotFound() {
        Long requestId = 1L;
        Long statusId = 2L;

        RequestClient client = new RequestClient();
        client.setId(requestId);
        client.setStatusId(1L);

        when(requestClientRepository.findById(requestId)).thenReturn(Mono.just(client));
        when(requestStatusRepository.findById(statusId)).thenReturn(Mono.empty());

        Mono<RequestClient> result = requestClientUseCase.updateStatus(requestId, statusId);

        StepVerifier.create(result)
                .expectErrorMessage("Estado con id " + statusId + " no encontrado")
                .verify();
    }

    @Test
    void updateStatus_StatusAlreadyCurrent() {
        Long requestId = 1L;
        Long statusId = 1L;

        RequestClient client = new RequestClient();
        client.setId(requestId);
        client.setStatusId(statusId);

        RequestStatus status = new RequestStatus();
        status.setId(statusId);
        status.setName("Aprobada");

        when(requestClientRepository.findById(requestId)).thenReturn(Mono.just(client));
        when(requestStatusRepository.findById(statusId)).thenReturn(Mono.just(status));

        Mono<RequestClient> result = requestClientUseCase.updateStatus(requestId, statusId);

        StepVerifier.create(result)
                .expectErrorMessage("La solicitud ya se encuentra en el estado solicitado")
                .verify();
    }

    @Test
    void saveRequest_whenAutomaticValidationTrue_shouldCallSendRequestForAutomaticValidation() {
        RequestClient requestClient = new RequestClient();
        requestClient.setId(1L);
        requestClient.setRequestTypeId(10L);
        requestClient.setIdentityDocument("123456789");
        requestClient.setEmail("test@example.com");
        requestClient.setAmount(1000.0);
        requestClient.setDeadline(12L);

        RequestType requestType = new RequestType();
        requestType.setId(10L);
        requestType.setAutomaticValidation(true);
        requestType.setInterestRate(0.18);

        UserInfo userInfo = new UserInfo();
        userInfo.setBaseSalary(3000000.0);

        when(requestTypeRepository.findById(10L)).thenReturn(Mono.just(requestType));
        when(requestClientRepository.save(any(RequestClient.class))).thenReturn(Mono.just(requestClient));
        when(requestTypeRepository.findAll()).thenReturn(Flux.just(requestType));
        when(requestClientRepository.findByEmailAndStatusId("test@example.com", 2L)).thenReturn(Flux.fromIterable(Collections.emptyList()));
        when(userInfoRepository.selfSearch("Bearer token")).thenReturn(Mono.just(userInfo));
        when(messageQueueRepository.sendMessageCalculateDebtCapacity(any())).thenReturn(Mono.empty());

        Mono<RequestClient> result = requestClientUseCase.saveRequest(requestClient, "Bearer token");

        StepVerifier.create(result)
                .expectNextMatches(saved -> saved.getId().equals(1L))
                .verifyComplete();

        verify(messageQueueRepository, times(1)).sendMessageCalculateDebtCapacity(any());
    }

    @Test
    void saveRequest_whenAutomaticValidationFalse_shouldNotCallSendRequestForAutomaticValidation() {
        RequestClient requestClient = new RequestClient();
        requestClient.setId(2L);
        requestClient.setRequestTypeId(20L);
        requestClient.setIdentityDocument("987654321");
        requestClient.setEmail("user@example.com");
        requestClient.setAmount(5000.0);
        requestClient.setDeadline(24L);

        RequestType requestType = new RequestType();
        requestType.setId(20L);
        requestType.setAutomaticValidation(false);

        when(requestTypeRepository.findById(20L)).thenReturn(Mono.just(requestType));
        when(requestClientRepository.save(any(RequestClient.class))).thenReturn(Mono.just(requestClient));

        Mono<RequestClient> result = requestClientUseCase.saveRequest(requestClient, "Bearer token");

        StepVerifier.create(result)
                .expectNextMatches(saved -> saved.getId().equals(2L))
                .verifyComplete();

        verify(messageQueueRepository, never()).sendMessageCalculateDebtCapacity(any());
    }

    @Test
    void updateStatusListener_Success() {
        Long requestId = 1L;
        Long statusId = 2L;

        RequestClient client = new RequestClient();
        client.setId(requestId);
        client.setStatusId(1L);
        client.setEmail("test@email.com");

        RequestStatus status = new RequestStatus();
        status.setId(statusId);
        status.setName("Aprobada");

        when(requestClientRepository.findById(requestId)).thenReturn(Mono.just(client));
        when(requestStatusRepository.findById(statusId)).thenReturn(Mono.just(status));
        when(requestClientRepository.save(any(RequestClient.class))).thenReturn(Mono.just(client));

        Mono<RequestClient> result = requestClientUseCase.updateStatusListener(requestId, statusId);

        StepVerifier.create(result)
                .expectNextMatches(savedClient -> savedClient.getStatusId().equals(statusId))
                .verifyComplete();
    }

    @Test
    void updateStatusListener_RequestNotFound() {
        Long requestId = 1L;
        Long statusId = 2L;

        when(requestClientRepository.findById(requestId)).thenReturn(Mono.empty());

        Mono<RequestClient> result = requestClientUseCase.updateStatusListener(requestId, statusId);

        StepVerifier.create(result)
                .expectErrorMessage("Solicitud con id " + requestId + " no encontrada")
                .verify();
    }

    @Test
    void updateStatusListener_StatusNotFound() {
        Long requestId = 1L;
        Long statusId = 2L;

        RequestClient client = new RequestClient();
        client.setId(requestId);
        client.setStatusId(1L);

        when(requestClientRepository.findById(requestId)).thenReturn(Mono.just(client));
        when(requestStatusRepository.findById(statusId)).thenReturn(Mono.empty());

        Mono<RequestClient> result = requestClientUseCase.updateStatusListener(requestId, statusId);

        StepVerifier.create(result)
                .expectErrorMessage("Estado con id " + statusId + " no encontrado")
                .verify();
    }

    @Test
    void updateStatusListener_StatusAlreadyCurrent() {
        Long requestId = 1L;
        Long statusId = 1L;

        RequestClient client = new RequestClient();
        client.setId(requestId);
        client.setStatusId(statusId);

        RequestStatus status = new RequestStatus();
        status.setId(statusId);
        status.setName("Aprobada");

        when(requestClientRepository.findById(requestId)).thenReturn(Mono.just(client));
        when(requestStatusRepository.findById(statusId)).thenReturn(Mono.just(status));

        Mono<RequestClient> result = requestClientUseCase.updateStatusListener(requestId, statusId);

        StepVerifier.create(result)
                .expectErrorMessage("La solicitud ya se encuentra en el estado solicitado")
                .verify();
    }

    @Test
    void totalByStatus_shouldReturnApprovedRequests() { 
        Long statusId = 1L;
        RequestStatus status = new RequestStatus();
        status.setId(statusId);
        status.setName("aprobada");

        when(requestStatusRepository.findById(statusId))
                .thenReturn(Mono.just(status));
        when(requestClientRepository.countByStatusId(statusId))
                .thenReturn(Mono.just(5L));

        Mono<ResponseDataTotal> result = requestClientUseCase.totalByStatus(TypeTotal.APPROVED_REQUESTS, statusId);

        StepVerifier.create(result)
                .expectNextMatches(r -> 
                        r.getStatus().equals("aprobada") &&
                        r.getType().equals(TypeTotal.APPROVED_REQUESTS.name()) &&
                        r.getValue().equals(5.0)
                )
                .verifyComplete();
    }

    @Test
    void totalByStatus_shouldReturnApprovedAmount() {
        Long statusId = 2L;
        RequestStatus status = new RequestStatus();
        status.setId(statusId);
        status.setName("aprobada");

        when(requestStatusRepository.findById(statusId))
                .thenReturn(Mono.just(status));
        when(requestClientRepository.sumAmountByStatusId(statusId))
                .thenReturn(Mono.just(1500.75));

        Mono<ResponseDataTotal> result = requestClientUseCase.totalByStatus(TypeTotal.APPROVED_AMOUNT, statusId);

        StepVerifier.create(result)
                .expectNextMatches(r -> 
                        r.getStatus().equals("aprobada") &&
                        r.getType().equals(TypeTotal.APPROVED_AMOUNT.name()) &&
                        r.getValue().equals(1500.75)
                )
                .verifyComplete();
    }

    @Test
    void totalByStatus_shouldReturnErrorWhenStatusNotFound() {
        Long statusId = 99L;
        when(requestStatusRepository.findById(statusId)).thenReturn(Mono.empty());

        Mono<ResponseDataTotal> result = requestClientUseCase.totalByStatus(TypeTotal.APPROVED_AMOUNT, statusId);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                                         e.getMessage().equals("El estado de préstamo no existe."))
                .verify();
    }

    @Test
    void totalsByStatus_shouldReturnBothTotals() {
        Long statusId = 1L;
        RequestStatus status = new RequestStatus();
        status.setId(statusId);
        status.setName("Aprobada");

        when(requestStatusRepository.findById(statusId)).thenReturn(Mono.just(status));
        when(requestClientRepository.countByStatusId(statusId)).thenReturn(Mono.just(5L));
        when(requestClientRepository.sumAmountByStatusId(statusId)).thenReturn(Mono.just(10000.0));

        Mono<List<ResponseDataTotal>> result = requestClientUseCase.totalsByStatus(statusId);

        StepVerifier.create(result)
                .expectNextMatches(list ->
                        list.size() == 2 &&
                        list.get(0).getType().equals(TypeTotal.APPROVED_REQUESTS.name()) &&
                        list.get(0).getValue() == 5.0 &&
                        list.get(1).getType().equals(TypeTotal.APPROVED_AMOUNT.name()) &&
                        list.get(1).getValue() == 10000.0
                )
                .verifyComplete();
    }

    @Test
    void totalsByStatus_shouldReturnErrorWhenStatusNotFound() {
        Long statusId = 99L;
        when(requestStatusRepository.findById(statusId)).thenReturn(Mono.empty());

        Mono<List<ResponseDataTotal>> result = requestClientUseCase.totalsByStatus(statusId);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("El estado de préstamo no existe."))
                .verify();
    }


}