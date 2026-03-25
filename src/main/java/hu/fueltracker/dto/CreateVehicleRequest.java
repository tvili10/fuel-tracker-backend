package hu.fueltracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {
    private String name;
    private String make;
    private String model;
    private String licensePlate;
    private String fuelType;
    private Double tankCapacity;
}

