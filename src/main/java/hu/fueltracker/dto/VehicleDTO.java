package hu.fueltracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private UUID id;
    private UUID userId;
    private String name;
    private String make;
    private String model;
    private String licensePlate;
    private String fuelType;
    private Double tankCapacity;
}

