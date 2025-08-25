package co.com.powerup.api.dtos.request;

public record RequestTypeCreateDTO (
     String name,
     Double minAmount,
     Double maxAmount,
     Double interestRate,
     Boolean automaticValidation
){}