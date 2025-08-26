package co.com.powerup.usecase.requeststatus;

import co.com.powerup.model.requeststatus.RequestStatus;
import co.com.powerup.model.requeststatus.gateways.RequestStatusRepository;
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
class RequestStatusUseCaseTest {

    @Mock
    private RequestStatusRepository requestStatusRepository;

    @InjectMocks
    private RequestStatusUseCase requestStatusUseCase;

    private RequestStatus validStatus;

    @BeforeEach
    void setUp() {
        validStatus = new RequestStatus();
        validStatus.setName("Aprobado");
        validStatus.setDescription("El request fue aprobado");
    }

    @Test
    void saveStatus_whenValid_shouldSaveSuccessfully() {
        when(requestStatusRepository.save(any(RequestStatus.class))).thenReturn(Mono.just(validStatus));

        StepVerifier.create(requestStatusUseCase.saveStatus(validStatus))
                .expectNextMatches(status -> status.getName().equals("Aprobado") &&
                                             status.getDescription().equals("El request fue aprobado"))
                .verifyComplete();

        verify(requestStatusRepository, times(1)).save(validStatus);
    }

    @Test
    void saveStatus_whenNameNull_shouldReturnError() {
        validStatus.setName(null);

        StepVerifier.create(requestStatusUseCase.saveStatus(validStatus))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'name' es obligatorio."))
                .verify();

        verify(requestStatusRepository, never()).save(any());
    }

    @Test
    void saveStatus_whenNameBlank_shouldReturnError() {
        validStatus.setName("");

        StepVerifier.create(requestStatusUseCase.saveStatus(validStatus))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'name' es obligatorio."))
                .verify();

        verify(requestStatusRepository, never()).save(any());
    }

    @Test
    void saveStatus_whenDescriptionNull_shouldReturnError() {
        validStatus.setDescription(null);

        StepVerifier.create(requestStatusUseCase.saveStatus(validStatus))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'description' es obligatorio."))
                .verify();

        verify(requestStatusRepository, never()).save(any());
    }

    @Test
    void saveStatus_whenDescriptionBlank_shouldReturnError() {
        validStatus.setDescription("");

        StepVerifier.create(requestStatusUseCase.saveStatus(validStatus))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'description' es obligatorio."))
                .verify();

        verify(requestStatusRepository, never()).save(any());
    }

    @Test
    void findAll_shouldReturnStatuses() {
        when(requestStatusRepository.findAll()).thenReturn(Flux.just(validStatus));

        StepVerifier.create(requestStatusUseCase.findAll())
                .expectNext(validStatus)
                .verifyComplete();

        verify(requestStatusRepository, times(1)).findAll();
    }
}

