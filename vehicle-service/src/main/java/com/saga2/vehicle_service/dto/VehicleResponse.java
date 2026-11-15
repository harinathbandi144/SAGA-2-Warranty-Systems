package com.saga2.vehicle_service.dto;

import com.saga2.vehicle_service.enums.VehicleStatus;

import java.time.LocalDate;

public record VehicleResponse(
        Long id,
        String vin,
        String brand,
        String model,
        Integer manufacturingYear,
        LocalDate warrantyStartDate,
        LocalDate warrantyEndDate,
        VehicleStatus status
) {}
