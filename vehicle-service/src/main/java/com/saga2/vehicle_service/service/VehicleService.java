package com.saga2.vehicle_service.service;


import com.saga2.vehicle_service.dto.CreateVehicleRequest;
import com.saga2.vehicle_service.dto.VehicleResponse;
import com.saga2.vehicle_service.dto.WarrantyEligibilityResponse;

public interface VehicleService {

    VehicleResponse registerVehicle(CreateVehicleRequest request);

    VehicleResponse getVehicleByVin(String vin);

    WarrantyEligibilityResponse checkWarrantyEligibility(String vin);
}
