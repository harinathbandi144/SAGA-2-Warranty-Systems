package com.saga2.vehicle_service.service;


import com.saga2.vehicle_service.dto.CreateVehicleRequest;
import com.saga2.vehicle_service.dto.VehicleResponse;
import com.saga2.vehicle_service.dto.WarrantyEligibilityResponse;
import com.saga2.vehicle_service.entity.Vehicle;
import com.saga2.vehicle_service.enums.VehicleStatus;
import com.saga2.vehicle_service.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public VehicleResponse registerVehicle(CreateVehicleRequest request){
        if(vehicleRepository.existsByVin(request.vin())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Vehicle already registered with VIN: " + request.vin());
        }
        if(request.warrantyEndDate().isBefore(request.warrantyStartDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Warranty end date cannot be before start date");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setVin(request.vin());
        vehicle.setBrand(request.brand());
        vehicle.setModel(request.model());
        vehicle.setManufacturingYear(request.manufacturingYear());
        vehicle.setWarrantyStartDate(request.warrantyStartDate());
        vehicle.setWarrantyEndDate(request.warrantyEndDate());

        Vehicle savedResponse = vehicleRepository.save(vehicle);
        return new VehicleResponse(savedResponse.getId(),
                savedResponse.getVin(),
                savedResponse.getBrand(),
                savedResponse.getModel(),
                savedResponse.getManufacturingYear(),
                savedResponse.getWarrantyStartDate(),
                savedResponse.getWarrantyEndDate(),
                savedResponse.getStatus());
    }

    @Override
    public VehicleResponse getVehicleByVin(String vin) {
        Vehicle vehicle = vehicleRepository.findByVin(vin).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found")
        );

        return toResponse(vehicle);
    }

    @Override
    public WarrantyEligibilityResponse checkWarrantyEligibility(String vin) {

        Vehicle vehicle = vehicleRepository.findByVin(vin).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found with VIN: " + vin));

        if(vehicle.getStatus() != VehicleStatus.ACTIVE){
            return new WarrantyEligibilityResponse(vin, false, "Vehicle is inactive");
        }

        LocalDate date = LocalDate.now();
        if(date.isBefore(vehicle.getWarrantyStartDate())){
            return new WarrantyEligibilityResponse(vin, false, "Warranty has not started");
        }

        if (date.isAfter(vehicle.getWarrantyEndDate())){
            return new WarrantyEligibilityResponse(vin, false, "Warranty has expired");
        }

        return new WarrantyEligibilityResponse(vin, true,  "Vehicle warranty is active");

    }

    private VehicleResponse toResponse(Vehicle vehicle){
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getVin(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getManufacturingYear(),
                vehicle.getWarrantyStartDate(),
                vehicle.getWarrantyEndDate(),
                vehicle.getStatus());
   }
}
