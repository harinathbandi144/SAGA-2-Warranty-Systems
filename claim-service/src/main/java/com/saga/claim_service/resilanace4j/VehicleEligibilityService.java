package com.saga.claim_service.resilanace4j;



import com.saga.claim_service.client.dto.WarrantyEligibilityResponse;
import com.saga.claim_service.client.service.VehicleClient;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class VehicleEligibilityService {

    private VehicleClient vehicleClient;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Retry(name = "vehicleService")
    @CircuitBreaker(name = "vehicleService", fallbackMethod = "fallback")
    public WarrantyEligibilityResponse checkEligibility(String vin){
        System.out.println("Calling Vehicle Service...");
        System.out.println(
                circuitBreakerRegistry
                        .circuitBreaker("vehicleService")
                        .getState()
        );
        return vehicleClient.checkEligibility(vin);

    }

    public WarrantyEligibilityResponse fallback(String vin, Exception ex){
        throw  new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Vehicle Service is unavailable");
    }

}
