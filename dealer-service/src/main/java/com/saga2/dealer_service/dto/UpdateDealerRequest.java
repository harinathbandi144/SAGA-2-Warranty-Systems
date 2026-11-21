package com.saga2.dealer_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDealerRequest(
        @NotBlank
        @Size(max = 150)
        String dealerName,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @NotBlank
        @Size(max = 20)
        String phone,

        @NotBlank
        @Size(max = 100)
        String city
) {
}
