package com.saga2.dealer_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDealerRequest(

        @NotBlank(message = "Dealer ID is required")
        @Size(max = 20)
        String dealerId,

        @NotBlank(message = "Dealer name is required")
        @Size(max = 150)
        String dealerName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email")
        String email,

        @NotBlank(message = "Phone is required")
        @Size(max = 20)
        String phone,

        @NotBlank(message = "City is required")
        String city

) {}
