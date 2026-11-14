package com.saga.claim_service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateClaimRequest(

        @NotBlank
        String dealerId,

        @NotBlank
        @Size(min = 17, max = 17)
        String vin,

        @NotBlank
        @Size(max = 1000)
        String failureDescription,

        @NotNull(message = "Claim amount is required")
        @DecimalMin(value = "0.01",
                message = "Claim amount must be greater than zero")
        @Digits(integer = 10, fraction = 2,
                message = "Invalid claim amount format")
        BigDecimal claimedAmount
) {
}
