package com.saga2.dealer_service.dto;

import com.saga2.dealer_service.enums.DealerStatus;

public record DealerEligibilityResponse(

        String dealerId,
        boolean eligible,
        DealerStatus status
) {
}
