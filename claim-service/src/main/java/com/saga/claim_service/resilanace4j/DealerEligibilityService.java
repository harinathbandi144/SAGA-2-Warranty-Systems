package com.saga.claim_service.resilanace4j;

import com.saga.claim_service.client.dto.DealerEligibilityResponse;
import com.saga.claim_service.client.service.DealerClient;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class DealerEligibilityService {

    private final DealerClient dealerClient;

    @Retry(name = "dealerService")
    @CircuitBreaker(name = "dealerService", fallbackMethod = "dealerFallback")
    public void validateDealer(String dealerId) {

        DealerEligibilityResponse response;

        try {
            response = dealerClient.checkEligibility(dealerId);

        } catch (FeignException.NotFound ex) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Dealer not found: " + dealerId,
                    ex
            );

        } catch (FeignException ex) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Dealer Service returned HTTP " + ex.status(),
                    ex
            );
        }

        if (response == null || !response.eligible()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Dealer is not eligible: " + dealerId
            );
        }
    }
    public void fallbackMethod(String dealerId, Throwable ex){
        if(ex instanceof ResponseStatusException responseEx){
            throw responseEx;
        }
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Dealer Service is temporarily unavailable",
                ex
        );
    }
}
