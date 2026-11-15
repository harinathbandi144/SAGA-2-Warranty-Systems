package com.saga2.vehicle_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateVehicleRequest(

        @NotBlank
        @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$",
                message = "VIN must contain 17 valid characters")
        String vin,

        @NotBlank String brand,

        @NotBlank String model,

        @NotNull
        @Min(1980)
        Integer manufacturingYear,

        @NotNull LocalDate warrantyStartDate,

        @NotNull LocalDate warrantyEndDate
) {}
