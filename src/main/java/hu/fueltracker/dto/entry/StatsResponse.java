package hu.fueltracker.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsResponse {
    private Double avgSpend;
    private Double avgLiters;
    private Double minSpend;
    private Double maxSpend;
    private Double minLiters;
    private Double maxLiters;
    private Double spendVariance;
    private Double spendStdDev;
    private Double litersVariance;
    private Double litersStdDev;
    private List<PeriodValue> fuelByEntry;
    private List<PeriodValue> monthlyCosts;
    private List<PeriodValue> yearlyCosts;
}

