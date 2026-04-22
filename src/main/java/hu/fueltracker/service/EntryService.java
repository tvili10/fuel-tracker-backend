package hu.fueltracker.service;

import hu.fueltracker.dto.entry.CreateEntryRequest;
import hu.fueltracker.dto.entry.EntryDTO;
import hu.fueltracker.dto.entry.PeriodValue;
import hu.fueltracker.dto.entry.StatsRequest;
import hu.fueltracker.dto.entry.StatsResponse;
import hu.fueltracker.entity.EntryEntity;
import hu.fueltracker.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EntryService {

    @Autowired
    private EntryRepository entryRepository;

    public EntryDTO createEntry(UUID userId, CreateEntryRequest request) {
        EntryEntity entry = new EntryEntity();
        entry.setUserId(userId);
        entry.setVehicleId(request.getVehicleId());
        entry.setAmount(request.getFuelQuantity());
        entry.setCost(request.getAmount());
        entry.setDate(request.getEntryDate());


        EntryEntity savedEntry = entryRepository.save(entry);
        return convertToDTO(savedEntry);
    }

    public EntryDTO getEntryById(UUID entryId) {
        EntryEntity entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found with id: " + entryId));
        return convertToDTO(entry);
    }

    public List<EntryDTO> getUserEntries(UUID userId) {
        return entryRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EntryDTO> getVehicleEntries(UUID vehicleId) {
        return entryRepository.findByVehicleId(vehicleId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EntryDTO updateEntry(UUID entryId, CreateEntryRequest request) {
        EntryEntity entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found with id: " + entryId));

        if (request.getVehicleId() != null) entry.setVehicleId(request.getVehicleId());
        if (request.getAmount() != null) entry.setCost(request.getAmount());
        if (request.getFuelQuantity() != null) entry.setAmount(request.getFuelQuantity());
        if (request.getEntryDate() != null) entry.setDate(request.getEntryDate());

        EntryEntity updatedEntry = entryRepository.save(entry);
        return convertToDTO(updatedEntry);
    }

    public void deleteEntry(UUID entryId) {
        EntryEntity entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found with id: " + entryId));
        entryRepository.delete(entry);
    }

    private EntryDTO convertToDTO(EntryEntity entry) {
        return new EntryDTO(
                entry.getId(),
                entry.getUserId(),
                entry.getVehicleId(),
                java.sql.Timestamp.valueOf(entry.getDate()),
                entry.getAmount(),
                entry.getCost()
        );
    }

    public StatsResponse computeStats(UUID vehicleId, StatsRequest request) {
        List<EntryEntity> entries = getFilteredEntries(vehicleId, request);

        StatsResponse response = StatsResponse.builder()
                .fuelByEntry(new ArrayList<>())
                .monthlyCosts(new ArrayList<>())
                .yearlyCosts(new ArrayList<>())
                .build();

        if (entries.isEmpty()) {
            return response;
        }

        // Calculate basic stats
        double[] costs = entries.stream()
                .filter(e -> e.getCost() != null && e.getCost() > 0)
                .mapToDouble(EntryEntity::getCost)
                .toArray();
        double[] liters = entries.stream()
                .filter(e -> e.getAmount() != null && e.getAmount() > 0)
                .mapToDouble(EntryEntity::getAmount)
                .toArray();

        if (costs.length > 0) {
            response.setAvgSpend(calculateMean(costs));
            response.setMinSpend(calculateMin(costs));
            response.setMaxSpend(calculateMax(costs));
            response.setSpendVariance(calculateVariance(costs));
            response.setSpendStdDev(calculateStdDev(costs));
        }

        if (liters.length > 0) {
            response.setAvgLiters(calculateMean(liters));
            response.setMinLiters(calculateMin(liters));
            response.setMaxLiters(calculateMax(liters));
            response.setLitersVariance(calculateVariance(liters));
            response.setLitersStdDev(calculateStdDev(liters));
        }

        // Build fuelByEntry (sorted by date)
        response.setFuelByEntry(
                entries.stream()
                        .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                        .map(e -> new PeriodValue(
                                e.getDate().toLocalDate().toString(),
                                e.getAmount()
                        ))
                        .collect(Collectors.toList())
        );

        // Build monthlyCosts (aggregated by year-month, sorted descending)
        Map<String, Double> monthlyCostsMap = new TreeMap<>();
        entries.forEach(e -> {
            String yearMonth = e.getDate().getYear() + "-" + String.format("%02d", e.getDate().getMonthValue());
            monthlyCostsMap.put(yearMonth, monthlyCostsMap.getOrDefault(yearMonth, 0.0) + (e.getCost() != null ? e.getCost() : 0.0));
        });

        response.setMonthlyCosts(
                monthlyCostsMap.entrySet().stream()
                        .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                        .map(e -> new PeriodValue(e.getKey(), e.getValue()))
                        .collect(Collectors.toList())
        );

        // Build yearlyCosts (aggregated by year, sorted descending)
        Map<String, Double> yearlyCostsMap = new TreeMap<>();
        entries.forEach(e -> {
            String year = String.valueOf(e.getDate().getYear());
            yearlyCostsMap.put(year, yearlyCostsMap.getOrDefault(year, 0.0) + (e.getCost() != null ? e.getCost() : 0.0));
        });

        response.setYearlyCosts(
                yearlyCostsMap.entrySet().stream()
                        .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                        .map(e -> new PeriodValue(e.getKey(), e.getValue()))
                        .collect(Collectors.toList())
        );

        return response;
    }

    private List<EntryEntity> getFilteredEntries(UUID vehicleId, StatsRequest request) {
        List<EntryEntity> entries = getVehicleEntries(vehicleId).stream()
                .map(dto -> {
                    EntryEntity entity = new EntryEntity();
                    entity.setId(dto.getId());
                    entity.setUserId(dto.getUserId());
                    entity.setVehicleId(dto.getVehicleId());
                    entity.setDate(dto.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                    entity.setAmount(dto.getAmount());
                    entity.setCost(dto.getCost());
                    return entity;
                })
                .collect(Collectors.toList());

        if ("all".equals(request.getPeriodPreset())) {
            return entries;
        }

        int currentYear = LocalDateTime.now().getYear();
        int year = request.getSelectedYear() != null ? request.getSelectedYear() : currentYear;

        if ("thisYear".equals(request.getPeriodPreset())) {
            year = currentYear;
            LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(year + 1, 1, 1, 0, 0, 0);
            return entries.stream()
                    .filter(e -> e.getDate().isAfter(startDate) && e.getDate().isBefore(endDate))
                    .collect(Collectors.toList());
        }

        if ("thisMonth".equals(request.getPeriodPreset())) {
            year = currentYear;
            int month = LocalDateTime.now().getMonthValue();
            LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
            LocalDateTime endDate = startDate.plusMonths(1);
            return entries.stream()
                    .filter(e -> e.getDate().isAfter(startDate) && e.getDate().isBefore(endDate))
                    .collect(Collectors.toList());
        }

        if ("custom".equals(request.getPeriodPreset())) {
            int month = request.getSelectedMonth() != null ? Integer.parseInt(request.getSelectedMonth()) : 1;
            LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
            LocalDateTime endDate = startDate.plusMonths(1);
            return entries.stream()
                    .filter(e -> e.getDate().isAfter(startDate) && e.getDate().isBefore(endDate))
                    .collect(Collectors.toList());
        }

        return entries;
    }

    private double calculateMean(double[] values) {
        if (values.length == 0) return 0;
        double sum = 0;
        for (double v : values) sum += v;
        return sum / values.length;
    }

    private double calculateMin(double[] values) {
        if (values.length == 0) return 0;
        double min = values[0];
        for (double v : values) if (v < min) min = v;
        return min;
    }

    private double calculateMax(double[] values) {
        if (values.length == 0) return 0;
        double max = values[0];
        for (double v : values) if (v > max) max = v;
        return max;
    }

    private double calculateVariance(double[] values) {
        if (values.length == 0) return 0;
        double mean = calculateMean(values);
        double sumSquares = 0;
        for (double v : values) {
            sumSquares += Math.pow(v - mean, 2);
        }
        return sumSquares / values.length;
    }

    private double calculateStdDev(double[] values) {
        return Math.sqrt(calculateVariance(values));
    }
}

