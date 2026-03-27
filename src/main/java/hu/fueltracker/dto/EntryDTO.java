package hu.fueltracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryDTO {
    private UUID id;
    private UUID userId;
    private UUID vehicleId;
    private Double amount;
    private Double fuelQuantity;
    private LocalDateTime entryDate;
    private Double odometerReading;
    private String notes;
    private LocalDateTime createdAt;
}

