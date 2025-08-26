package co.com.powerup.usecase.requesttype;

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
class RequestTypeUseCaseTest {

    @Mock
    private RequestTypeRepository requestTypeRepository;

    @InjectMocks
    private RequestTypeUseCase requestTypeUseCase;

    private RequestType validType;

    @BeforeEach
    void setUp() {
        validType = new RequestType();
        validType.setName("Préstamo Personal");
        validType.setMinAmount(1000.0);
        validType.setMaxAmount(10000.0);
        validType.setInterestRate(5.0);
        validType.setAutomaticValidation(true);
    }

    @Test
    void saveType_whenValid_shouldSaveSuccessfully() {
        when(requestTypeRepository.save(any(RequestType.class))).thenReturn(Mono.just(validType));

        StepVerifier.create(requestTypeUseCase.saveType(validType))
                .expectNextMatches(type -> type.getName().equals("Préstamo Personal") &&
                                           type.getMinAmount() == 1000.0 &&
                                           type.getMaxAmount() == 10000.0 &&
                                           type.getInterestRate() == 5.0 &&
                                           type.getAutomaticValidation())
                .verifyComplete();

        verify(requestTypeRepository, times(1)).save(validType);
    }

    @Test
    void saveType_whenNameNullOrBlank_shouldReturnError() {
        validType.setName(null);
        StepVerifier.create(requestTypeUseCase.saveType(validType))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'name' es obligatorio."))
                .verify();

        validType.setName("");
        StepVerifier.create(requestTypeUseCase.saveType(validType))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'name' es obligatorio."))
                .verify();

        verify(requestTypeRepository, never()).save(any());
    }

    @Test
    void saveType_whenMinAmountInvalid_shouldReturnError() {
        validType.setMinAmount(null);
        StepVerifier.create(requestTypeUseCase.saveType(validType))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'minAmount' es obligatorio y debe ser mayor a 0."))
                .verify();

        validType.setMinAmount(0.0);
        StepVerifier.create(requestTypeUseCase.saveType(validType))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'minAmount' es obligatorio y debe ser mayor a 0."))
                .verify();

        verify(requestTypeRepository, never()).save(any());
    }

    @Test
    void saveType_whenMaxAmountInvalid_shouldReturnError() {
        validType.setMaxAmount(null);
        StepVerifier.create(requestTypeUseCase.saveType(validType))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'maxAmount' es obligatorio y debe ser mayor a 0."))
                .verify();

        validType.setMaxAmount(0.0);
        StepVerifier.create(requestTypeUseCase.saveType(validType))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'maxAmount' es obligatorio y debe ser mayor a 0."))
                .verify();

        verify(requestTypeRepository, never()).save(any());
    }

    @Test
    void saveType_whenInterestRateInvalid_shouldReturnError() {
        validType.setInterestRate(null);
        StepVerifier.create(requestTypeUseCase.saveType(validType))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'interestRate' es obligatorio y debe ser mayor a 0."))
                .verify();

        validType.setInterestRate(0.0);
        StepVerifier.create(requestTypeUseCase.saveType(validType))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'interestRate' es obligatorio y debe ser mayor a 0."))
                .verify();

        verify(requestTypeRepository, never()).save(any());
    }

    @Test
    void saveType_whenAutomaticValidationNull_shouldReturnError() {
        validType.setAutomaticValidation(null);

        StepVerifier.create(requestTypeUseCase.saveType(validType))
                .expectErrorMatches(err -> err instanceof IllegalArgumentException &&
                        err.getMessage().equals("El campo 'automaticValidation' es obligatorio."))
                .verify();

        verify(requestTypeRepository, never()).save(any());
    }

    @Test
    void findAll_shouldReturnTypes() {
        when(requestTypeRepository.findAll()).thenReturn(Flux.just(validType));

        StepVerifier.create(requestTypeUseCase.findAll())
                .expectNext(validType)
                .verifyComplete();

        verify(requestTypeRepository, times(1)).findAll();
    }
}

