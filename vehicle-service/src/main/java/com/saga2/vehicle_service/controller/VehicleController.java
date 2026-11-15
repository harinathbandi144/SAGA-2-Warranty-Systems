package com.saga2.vehicle_service.controller;

import com.saga2.vehicle_service.dto.CreateVehicleRequest;
import com.saga2.vehicle_service.dto.VehicleResponse;
import com.saga2.vehicle_service.dto.WarrantyEligibilityResponse;
import com.saga2.vehicle_service.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponse> registerVehicle(@Valid @RequestBody CreateVehicleRequest request){

        VehicleResponse response = vehicleService.registerVehicle(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/vin/{vin}")
    public ResponseEntity<VehicleResponse> getVehicleByVin(@PathVariable String vin){

        VehicleResponse response = vehicleService.getVehicleByVin(vin);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/vin/{vin}/eligibility")
    public ResponseEntity<WarrantyEligibilityResponse> checkWarrantyEligibility(@PathVariable String vin){
       return ResponseEntity.ok(vehicleService.checkWarrantyEligibility(vin));
     }
}
