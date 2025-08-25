package co.com.powerup.api;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.com.powerup.api.dtos.response.ErrorResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
@Order(-2)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleAllExceptions(Exception ex, ServerHttpRequest request) {
        return Mono.just(ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .path(request.getPath().toString())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<co.com.powerup.api.dtos.response.ErrorResponse> handleBadRequest(IllegalArgumentException ex, ServerHttpRequest request) {
        return Mono.just(ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getPath().toString())
                .timestamp(LocalDateTime.now())
                .build());
    }
    
}
