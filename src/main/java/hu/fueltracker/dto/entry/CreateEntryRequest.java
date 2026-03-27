package hu.fueltracker.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEntryRequest {
    private UUID vehicleId;
    private Double amount;
    private Double fuelQuantity;
    private LocalDateTime entryDate;
    private Double odometerReading;
    private String notes;
}

