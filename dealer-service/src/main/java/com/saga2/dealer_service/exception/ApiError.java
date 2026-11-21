package com.saga2.dealer_service.exception;

import java.time.OffsetDateTime;

public record ApiError(
        int status,
        String message,
        OffsetDateTime timestamp
) {
}
