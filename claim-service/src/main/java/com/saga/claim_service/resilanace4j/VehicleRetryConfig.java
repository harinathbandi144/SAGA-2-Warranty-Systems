package com.saga.claim_service.resilanace4j;

import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class VehicleRetryConfig {

    @Bean
    public Retry vehicleRetry() {

        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .retryOnException(ex ->
                        ex instanceof RetryableException
                                || (ex instanceof FeignException fe
                                && fe.status() >= 500)
                )
                .build();

        return Retry.of("vehicleRetry", config);
    }
}
