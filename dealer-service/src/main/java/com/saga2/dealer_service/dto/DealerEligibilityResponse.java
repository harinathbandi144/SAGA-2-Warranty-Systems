package com.saga2.dealer_service.dto;

import com.saga2.dealer_service.enums.DealerStatus;

import java.io.Serializable;

public record DealerEligibilityResponse(

        String dealerId,
        boolean eligible,
        DealerStatus status
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
