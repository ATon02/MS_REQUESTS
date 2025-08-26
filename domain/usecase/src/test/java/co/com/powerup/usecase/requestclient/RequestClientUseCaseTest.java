package co.com.powerup.usecase.requestclient;

import co.com.powerup.model.requestclient.RequestClient;
import co.com.powerup.model.requestclient.gateways.RequestClientRepository;
import co.com.powerup.model.requesttype.RequestType;
import co.com.powerup.model.requesttype.gateways.RequestTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestClientUseCaseTest {

    @Mock
    private RequestClientRepository requestClientRepository;

    @Mock
    private RequestTypeRepository requestTypeRepository;

    @InjectMocks
    private RequestClientUseCase requestClientUseCase;

    private RequestClient validRequest;

    @BeforeEach
    void setUp() {
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

        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectNextMatches(req -> req.getEmail().equals("cliente@example.com") &&
                                           req.getStatusId() == 1L)
                .verifyComplete();

        verify(requestTypeRepository, times(1)).findById(1L);
        verify(requestClientRepository, times(1)).save(validRequest);
    }

    @Test
    void saveRequest_whenRequestTypeIdNull_shouldReturnError() {
        validRequest.setRequestTypeId(null);

        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El tipo de préstamo es obligatorio."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenIdentityDocumentNullOrBlank_shouldReturnError() {
        validRequest.setIdentityDocument(null);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El documento de identidad del cliente es obligatorio."))
                .verify();

        validRequest.setIdentityDocument("");
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El documento de identidad del cliente es obligatorio."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenEmailNullOrBlank_shouldReturnError() {
        validRequest.setEmail(null);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El email es obligatorio."))
                .verify();

        validRequest.setEmail("");
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El email es obligatorio."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenEmailInvalid_shouldReturnError() {
        validRequest.setEmail("correo-invalido");

        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El email tiene un formato inválido."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenAmountInvalid_shouldReturnError() {
        validRequest.setAmount(null);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("Monto no válido, debe ser mayor a 0."))
                .verify();

        validRequest.setAmount(0.0);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("Monto no válido, debe ser mayor a 0."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenDeadlineInvalid_shouldReturnError() {
        validRequest.setDeadline(null);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El plazo debe ser mayor a 0."))
                .verify();

        validRequest.setDeadline(0L);
        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El plazo debe ser mayor a 0."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenIdentityDocumentInvalid_shouldReturnError() {
        validRequest.setIdentityDocument("ABC123");

        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El documento de identidad solo debe contener números."))
                .verify();

        verify(requestClientRepository, never()).save(any());
    }

    @Test
    void saveRequest_whenRequestTypeDoesNotExist_shouldReturnError() {
        when(requestTypeRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(requestClientUseCase.saveRequest(validRequest))
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
}