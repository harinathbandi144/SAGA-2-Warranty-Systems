package com.saga2.dealer_service.dto;

import com.saga2.dealer_service.enums.DealerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateDealerStatusRequest(

        @NotNull(message = "Dealer Status is required")
        DealerStatus status
) {
}
