package com.saga.claim_service.client.service;

import com.saga.claim_service.client.dto.DealerEligibilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "dealer-service",
        url = "${services.dealer.url}"
)
public interface DealerClient {

    @GetMapping("/api/v1/dealers/{dealerId}/eligibility")
    DealerEligibilityResponse checkEligibility(
            @PathVariable("dealerId") String dealerId
    );
}
