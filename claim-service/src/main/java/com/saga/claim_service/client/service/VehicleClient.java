package com.saga.claim_service.client.service;


import com.saga.claim_service.client.dto.WarrantyEligibilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vehicle-service", url = "${services.vehicle.url}")

public interface VehicleClient{

    @GetMapping("/api/v1/vehicles/vin/{vin}/eligibility")
    WarrantyEligibilityResponse checkEligibility(@PathVariable("vin") String vin);
}
