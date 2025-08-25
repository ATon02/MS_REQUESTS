package co.com.powerup.api.dtos.request;

public record RequestClientCreateDTO(
    Double amount,
    Long deadline,
    String email,
    Long requestTypeId,
    String identityDocument
) {}