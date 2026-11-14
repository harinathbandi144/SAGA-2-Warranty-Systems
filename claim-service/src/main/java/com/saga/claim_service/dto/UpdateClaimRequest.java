package com.saga.claim_service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateClaimRequest(

        @NotBlank
        @Size(max = 1000)
        String failureDescription,

        @NotNull
        @DecimalMin("0.01")
        @Digits(integer = 10, fraction = 2)
        BigDecimal claimedAmount


) {
}
