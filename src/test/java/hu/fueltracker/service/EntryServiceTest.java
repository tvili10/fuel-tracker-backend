package hu.fueltracker.service;

import hu.fueltracker.dto.entry.CreateEntryRequest;
import hu.fueltracker.dto.entry.EntryDTO;
import hu.fueltracker.dto.entry.StatsRequest;
import hu.fueltracker.dto.entry.StatsResponse;
import hu.fueltracker.entity.EntryEntity;
import hu.fueltracker.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntryServiceTest {

    @Mock
    private EntryRepository entryRepository;

    @InjectMocks
    private EntryService entryService;

    private UUID userId;
    private UUID vehicleId;
    private UUID entryId;
    private EntryEntity entryEntity;
    private CreateEntryRequest createEntryRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        vehicleId = UUID.randomUUID();
        entryId = UUID.randomUUID();

        createEntryRequest = new CreateEntryRequest();
        createEntryRequest.setVehicleId(vehicleId);
        createEntryRequest.setAmount(50.0);
        createEntryRequest.setFuelQuantity(30.0);
        createEntryRequest.setEntryDate(LocalDateTime.now());

        entryEntity = new EntryEntity();
        entryEntity.setId(entryId);
        entryEntity.setUserId(userId);
        entryEntity.setVehicleId(vehicleId);
        entryEntity.setDate(LocalDateTime.now());
        entryEntity.setAmount(30.0);
        entryEntity.setCost(50.0);
    }

    @Test
    void testCreateEntry() {
        when(entryRepository.save(any(EntryEntity.class))).thenReturn(entryEntity);

        EntryDTO result = entryService.createEntry(userId, createEntryRequest);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(vehicleId, result.getVehicleId());
        assertEquals(30.0, result.getAmount());
        assertEquals(50.0, result.getCost());
        verify(entryRepository, times(1)).save(any(EntryEntity.class));
    }

    @Test
    void testGetEntryById() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.of(entryEntity));

        EntryDTO result = entryService.getEntryById(entryId);

        assertNotNull(result);
        assertEquals(entryId, result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals(30.0, result.getAmount());
        verify(entryRepository, times(1)).findById(entryId);
    }

    @Test
    void testGetEntryByIdNotFound() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> entryService.getEntryById(entryId));
        verify(entryRepository, times(1)).findById(entryId);
    }

    @Test
    void testGetUserEntries() {
        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(userId);
        entry2.setVehicleId(vehicleId);
        entry2.setDate(LocalDateTime.now());
        entry2.setAmount(25.0);
        entry2.setCost(40.0);

        when(entryRepository.findByUserId(userId)).thenReturn(Arrays.asList(entryEntity, entry2));

        List<EntryDTO> results = entryService.getUserEntries(userId);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(30.0, results.get(0).getAmount());
        assertEquals(25.0, results.get(1).getAmount());
        verify(entryRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetUserEntriesEmpty() {
        when(entryRepository.findByUserId(userId)).thenReturn(Arrays.asList());

        List<EntryDTO> results = entryService.getUserEntries(userId);

        assertNotNull(results);
        assertEquals(0, results.size());
        verify(entryRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetVehicleEntries() {
        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(UUID.randomUUID());
        entry2.setVehicleId(vehicleId);
        entry2.setDate(LocalDateTime.now());
        entry2.setAmount(20.0);
        entry2.setCost(35.0);

        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(Arrays.asList(entryEntity, entry2));

        List<EntryDTO> results = entryService.getVehicleEntries(vehicleId);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(vehicleId, results.get(0).getVehicleId());
        assertEquals(vehicleId, results.get(1).getVehicleId());
        verify(entryRepository, times(1)).findByVehicleId(vehicleId);
    }

    @Test
    void testGetVehicleEntriesEmpty() {
        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(Arrays.asList());

        List<EntryDTO> results = entryService.getVehicleEntries(vehicleId);

        assertNotNull(results);
        assertEquals(0, results.size());
        verify(entryRepository, times(1)).findByVehicleId(vehicleId);
    }

    @Test
    void testUpdateEntry() {
        CreateEntryRequest updateRequest = new CreateEntryRequest();
        updateRequest.setVehicleId(vehicleId);
        updateRequest.setAmount(60.0);
        updateRequest.setFuelQuantity(35.0);
        updateRequest.setEntryDate(LocalDateTime.now());

        EntryEntity updatedEntity = new EntryEntity();
        updatedEntity.setId(entryId);
        updatedEntity.setUserId(userId);
        updatedEntity.setVehicleId(vehicleId);
        updatedEntity.setDate(LocalDateTime.now());
        updatedEntity.setAmount(35.0);
        updatedEntity.setCost(60.0);

        when(entryRepository.findById(entryId)).thenReturn(Optional.of(entryEntity));
        when(entryRepository.save(any(EntryEntity.class))).thenReturn(updatedEntity);

        EntryDTO result = entryService.updateEntry(entryId, updateRequest);

        assertNotNull(result);
        assertEquals(35.0, result.getAmount());
        assertEquals(60.0, result.getCost());
        verify(entryRepository, times(1)).findById(entryId);
        verify(entryRepository, times(1)).save(any(EntryEntity.class));
    }

    @Test
    void testUpdateEntryPartial() {
        CreateEntryRequest updateRequest = new CreateEntryRequest();
        updateRequest.setAmount(55.0);
        // Other fields are null

        EntryEntity updatedEntity = new EntryEntity();
        updatedEntity.setId(entryId);
        updatedEntity.setUserId(userId);
        updatedEntity.setVehicleId(vehicleId);
        updatedEntity.setDate(LocalDateTime.now());
        updatedEntity.setAmount(30.0);
        updatedEntity.setCost(55.0);

        when(entryRepository.findById(entryId)).thenReturn(Optional.of(entryEntity));
        when(entryRepository.save(any(EntryEntity.class))).thenReturn(updatedEntity);

        EntryDTO result = entryService.updateEntry(entryId, updateRequest);

        assertNotNull(result);
        assertEquals(30.0, result.getAmount());
        assertEquals(55.0, result.getCost());
        verify(entryRepository, times(1)).findById(entryId);
        verify(entryRepository, times(1)).save(any(EntryEntity.class));
    }

    @Test
    void testUpdateEntryNotFound() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> entryService.updateEntry(entryId, createEntryRequest));
        verify(entryRepository, times(1)).findById(entryId);
    }

    @Test
    void testDeleteEntry() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.of(entryEntity));
        doNothing().when(entryRepository).delete(entryEntity);

        entryService.deleteEntry(entryId);

        verify(entryRepository, times(1)).findById(entryId);
        verify(entryRepository, times(1)).delete(entryEntity);
    }

    @Test
    void testDeleteEntryNotFound() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> entryService.deleteEntry(entryId));
        verify(entryRepository, times(1)).findById(entryId);
    }

    @Test
    void testComputeStatsAll() {
        LocalDateTime now = LocalDateTime.now();
        
        EntryEntity entry1 = new EntryEntity();
        entry1.setId(UUID.randomUUID());
        entry1.setUserId(userId);
        entry1.setVehicleId(vehicleId);
        entry1.setDate(now.minusDays(10));
        entry1.setAmount(30.0);
        entry1.setCost(50.0);

        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(userId);
        entry2.setVehicleId(vehicleId);
        entry2.setDate(now.minusDays(5));
        entry2.setAmount(40.0);
        entry2.setCost(70.0);

        EntryEntity entry3 = new EntryEntity();
        entry3.setId(UUID.randomUUID());
        entry3.setUserId(userId);
        entry3.setVehicleId(vehicleId);
        entry3.setDate(now);
        entry3.setAmount(35.0);
        entry3.setCost(60.0);

        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(Arrays.asList(entry1, entry2, entry3));

        StatsRequest request = new StatsRequest();
        request.setPeriodPreset("all");

        StatsResponse response = entryService.computeStats(vehicleId, request);

        assertNotNull(response);
        assertNotNull(response.getAvgSpend());
        assertNotNull(response.getAvgLiters());
        assertEquals(60.0, response.getAvgSpend()); // (50 + 70 + 60) / 3
        assertEquals(35.0, response.getAvgLiters()); // (30 + 40 + 35) / 3
        assertEquals(50.0, response.getMinSpend());
        assertEquals(70.0, response.getMaxSpend());
        assertEquals(30.0, response.getMinLiters());
        assertEquals(40.0, response.getMaxLiters());
        assertNotNull(response.getSpendVariance());
        assertNotNull(response.getSpendStdDev());
        assertNotNull(response.getLitersVariance());
        assertNotNull(response.getLitersStdDev());
        assertEquals(3, response.getFuelByEntry().size());
        assertFalse(response.getMonthlyCosts().isEmpty());
        assertFalse(response.getYearlyCosts().isEmpty());
    }

    @Test
    void testComputeStatsEmpty() {
        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(Arrays.asList());

        StatsRequest request = new StatsRequest();
        request.setPeriodPreset("all");

        StatsResponse response = entryService.computeStats(vehicleId, request);

        assertNotNull(response);
        assertNull(response.getAvgSpend());
        assertNull(response.getAvgLiters());
        assertTrue(response.getFuelByEntry().isEmpty());
        assertTrue(response.getMonthlyCosts().isEmpty());
        assertTrue(response.getYearlyCosts().isEmpty());
    }

    @Test
    void testComputeStatsThisYear() {
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        
        EntryEntity entry1 = new EntryEntity();
        entry1.setId(UUID.randomUUID());
        entry1.setUserId(userId);
        entry1.setVehicleId(vehicleId);
        entry1.setDate(LocalDateTime.of(currentYear, 1, 15, 10, 0));
        entry1.setAmount(30.0);
        entry1.setCost(50.0);

        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(userId);
        entry2.setVehicleId(vehicleId);
        entry2.setDate(LocalDateTime.of(currentYear - 1, 12, 15, 10, 0)); // Previous year, should be excluded
        entry2.setAmount(40.0);
        entry2.setCost(70.0);

        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(Arrays.asList(entry1, entry2));

        StatsRequest request = new StatsRequest();
        request.setPeriodPreset("thisYear");

        StatsResponse response = entryService.computeStats(vehicleId, request);

        assertNotNull(response);
        assertEquals(1, response.getFuelByEntry().size());
        assertEquals(30.0, response.getAvgLiters());
        assertEquals(50.0, response.getAvgSpend());
    }

    @Test
    void testComputeStatsThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        
        EntryEntity entry1 = new EntryEntity();
        entry1.setId(UUID.randomUUID());
        entry1.setUserId(userId);
        entry1.setVehicleId(vehicleId);
        entry1.setDate(LocalDateTime.of(currentYear, currentMonth, 15, 10, 0));
        entry1.setAmount(30.0);
        entry1.setCost(50.0);

        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(userId);
        entry2.setVehicleId(vehicleId);
        entry2.setDate(LocalDateTime.of(currentYear, currentMonth - 1 > 0 ? currentMonth - 1 : 12, 15, 10, 0)); // Previous month
        entry2.setAmount(40.0);
        entry2.setCost(70.0);

        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(Arrays.asList(entry1, entry2));

        StatsRequest request = new StatsRequest();
        request.setPeriodPreset("thisMonth");

        StatsResponse response = entryService.computeStats(vehicleId, request);

        assertNotNull(response);
        assertEquals(1, response.getFuelByEntry().size());
        assertEquals(30.0, response.getAvgLiters());
        assertEquals(50.0, response.getAvgSpend());
    }

    @Test
    void testComputeStatsCustom() {
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        
        EntryEntity entry1 = new EntryEntity();
        entry1.setId(UUID.randomUUID());
        entry1.setUserId(userId);
        entry1.setVehicleId(vehicleId);
        entry1.setDate(LocalDateTime.of(currentYear, 4, 15, 10, 0)); // April
        entry1.setAmount(30.0);
        entry1.setCost(50.0);

        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(userId);
        entry2.setVehicleId(vehicleId);
        entry2.setDate(LocalDateTime.of(currentYear, 5, 15, 10, 0)); // May, should be excluded
        entry2.setAmount(40.0);
        entry2.setCost(70.0);

        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(Arrays.asList(entry1, entry2));

        StatsRequest request = new StatsRequest();
        request.setPeriodPreset("custom");
        request.setSelectedYear(currentYear);
        request.setSelectedMonth("04");

        StatsResponse response = entryService.computeStats(vehicleId, request);

        assertNotNull(response);
        assertEquals(1, response.getFuelByEntry().size());
        assertEquals(30.0, response.getAvgLiters());
        assertEquals(50.0, response.getAvgSpend());
    }

    @Test
    void testComputStatsFuelByEntryOrdering() {
        LocalDateTime now = LocalDateTime.now();
        
        EntryEntity entry1 = new EntryEntity();
        entry1.setId(UUID.randomUUID());
        entry1.setUserId(userId);
        entry1.setVehicleId(vehicleId);
        entry1.setDate(now.minusDays(5));
        entry1.setAmount(30.0);
        entry1.setCost(50.0);

        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(userId);
        entry2.setVehicleId(vehicleId);
        entry2.setDate(now);
        entry2.setAmount(40.0);
        entry2.setCost(70.0);

        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(Arrays.asList(entry2, entry1)); // Reverse order

        StatsRequest request = new StatsRequest();
        request.setPeriodPreset("all");

        StatsResponse response = entryService.computeStats(vehicleId, request);

        // Should be ordered by date
        assertEquals(2, response.getFuelByEntry().size());
        assertEquals(30.0, response.getFuelByEntry().get(0).getValue());
        assertEquals(40.0, response.getFuelByEntry().get(1).getValue());
    }

    @Test
    void testComputStatsMonthlyCostsAggregation() {
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        
        EntryEntity entry1 = new EntryEntity();
        entry1.setId(UUID.randomUUID());
        entry1.setUserId(userId);
        entry1.setVehicleId(vehicleId);
        entry1.setDate(LocalDateTime.of(currentYear, 4, 1, 10, 0));
        entry1.setAmount(30.0);
        entry1.setCost(50.0);

        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(userId);
        entry2.setVehicleId(vehicleId);
        entry2.setDate(LocalDateTime.of(currentYear, 4, 15, 10, 0)); // Same month
        entry2.setAmount(40.0);
        entry2.setCost(70.0);

        EntryEntity entry3 = new EntryEntity();
        entry3.setId(UUID.randomUUID());
        entry3.setUserId(userId);
        entry3.setVehicleId(vehicleId);
        entry3.setDate(LocalDateTime.of(currentYear, 5, 1, 10, 0)); // Different month
        entry3.setAmount(35.0);
        entry3.setCost(60.0);

        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(Arrays.asList(entry1, entry2, entry3));

        StatsRequest request = new StatsRequest();
        request.setPeriodPreset("all");

        StatsResponse response = entryService.computeStats(vehicleId, request);

        assertEquals(2, response.getMonthlyCosts().size());
        // Should be sorted descending
        assertEquals(currentYear + "-05", response.getMonthlyCosts().get(0).getPeriod());
        assertEquals(60.0, response.getMonthlyCosts().get(0).getValue());
        assertEquals(currentYear + "-04", response.getMonthlyCosts().get(1).getPeriod());
        assertEquals(120.0, response.getMonthlyCosts().get(1).getValue()); // 50 + 70
    }

    @Test
    void testComputStatsYearlyCostsAggregation() {
        EntryEntity entry1 = new EntryEntity();
        entry1.setId(UUID.randomUUID());
        entry1.setUserId(userId);
        entry1.setVehicleId(vehicleId);
        entry1.setDate(LocalDateTime.of(2025, 6, 1, 10, 0));
        entry1.setAmount(30.0);
        entry1.setCost(50.0);

        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(userId);
        entry2.setVehicleId(vehicleId);
        entry2.setDate(LocalDateTime.of(2026, 3, 15, 10, 0));
        entry2.setAmount(40.0);
        entry2.setCost(70.0);

        EntryEntity entry3 = new EntryEntity();
        entry3.setId(UUID.randomUUID());
        entry3.setUserId(userId);
        entry3.setVehicleId(vehicleId);
        entry3.setDate(LocalDateTime.of(2026, 8, 1, 10, 0));
        entry3.setAmount(35.0);
        entry3.setCost(60.0);

        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(Arrays.asList(entry1, entry2, entry3));

        StatsRequest request = new StatsRequest();
        request.setPeriodPreset("all");

        StatsResponse response = entryService.computeStats(vehicleId, request);

        assertEquals(2, response.getYearlyCosts().size());
        // Should be sorted descending
        assertEquals("2026", response.getYearlyCosts().get(0).getPeriod());
        assertEquals(130.0, response.getYearlyCosts().get(0).getValue()); // 70 + 60
        assertEquals("2025", response.getYearlyCosts().get(1).getPeriod());
        assertEquals(50.0, response.getYearlyCosts().get(1).getValue());
    }
}

