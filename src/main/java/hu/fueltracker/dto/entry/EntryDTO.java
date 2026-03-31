package hu.fueltracker.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryDTO {
    private UUID id;
    private UUID userId;
    private UUID vehicleId;
    private Date date;
    private Double amount;
    private Double cost;
}

