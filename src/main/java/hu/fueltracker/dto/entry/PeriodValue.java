package hu.fueltracker.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodValue {
    private String period;
    private Double value;
}

