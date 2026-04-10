package hu.fueltracker.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsRequest {
    private String periodPreset; // "all", "thisYear", "thisMonth", "custom"
    private Integer selectedYear;
    private String selectedMonth; // "04" format or null
}

