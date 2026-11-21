package com.saga2.dealer_service.dto;

import com.saga2.dealer_service.enums.DealerStatus;

import java.time.OffsetDateTime;

public record DealerResponse(
        String dealerId,
        String dealerName,
        String email,
        String phone,
        String city,
        DealerStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
