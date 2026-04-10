package hu.fueltracker.controller;

import hu.fueltracker.dto.entry.CreateEntryRequest;
import hu.fueltracker.dto.entry.EntryDTO;
import hu.fueltracker.dto.entry.StatsRequest;
import hu.fueltracker.dto.entry.StatsResponse;
import hu.fueltracker.service.EntryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrtyControllerTest {

    @Mock
    private EntryService entryService;

    @InjectMocks
    private EnrtyController entrtyController;

    private UUID userId;
    private UUID vehicleId;
    private UUID entryId;
    private EntryDTO entryDTO;
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

        entryDTO = new EntryDTO();
        entryDTO.setId(entryId);
        entryDTO.setUserId(userId);
        entryDTO.setVehicleId(vehicleId);
        entryDTO.setDate(new Date());
        entryDTO.setAmount(30.0);
        entryDTO.setCost(50.0);
    }

    @Test
    void testAddEntry() {
        when(entryService.createEntry(eq(userId), any(CreateEntryRequest.class)))
                .thenReturn(entryDTO);

        ResponseEntity<EntryDTO> response = entrtyController.addEntry(userId.toString(), createEntryRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(30.0, response.getBody().getAmount());
        assertEquals(50.0, response.getBody().getCost());
        verify(entryService, times(1)).createEntry(eq(userId), any(CreateEntryRequest.class));
    }

    @Test
    void testDeleteEntry() {
        doNothing().when(entryService).deleteEntry(entryId);

        ResponseEntity<Void> response = entrtyController.deleteEntry(entryId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(entryService, times(1)).deleteEntry(entryId);
    }

    @Test
    void testListUserEntries() {
        EntryDTO entry2 = new EntryDTO();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(userId);
        entry2.setVehicleId(vehicleId);
        entry2.setDate(new Date());
        entry2.setAmount(25.0);
        entry2.setCost(40.0);

        List<EntryDTO> entries = Arrays.asList(entryDTO, entry2);
        when(entryService.getUserEntries(userId)).thenReturn(entries);

        ResponseEntity<List<EntryDTO>> response = entrtyController.listUserEntries(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(entryService, times(1)).getUserEntries(userId);
    }

    @Test
    void testListVehicleEntries() {
        List<EntryDTO> entries = Arrays.asList(entryDTO);
        when(entryService.getVehicleEntries(vehicleId)).thenReturn(entries);

        ResponseEntity<List<EntryDTO>> response = entrtyController.listVehicleEntries(vehicleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(entryService, times(1)).getVehicleEntries(vehicleId);
    }

    @Test
    void testGetEntry() {
        when(entryService.getEntryById(entryId)).thenReturn(entryDTO);

        ResponseEntity<EntryDTO> response = entrtyController.getEntry(entryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(30.0, response.getBody().getAmount());
        verify(entryService, times(1)).getEntryById(entryId);
    }

    @Test
    void testUpdateEntry() {
        when(entryService.updateEntry(eq(entryId), any(CreateEntryRequest.class)))
                .thenReturn(entryDTO);

        ResponseEntity<EntryDTO> response = entrtyController.updateEntry(entryId, createEntryRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(entryId, response.getBody().getId());
        verify(entryService, times(1)).updateEntry(eq(entryId), any(CreateEntryRequest.class));
    }

    @Test
    void testGetVehicleStats() {
        StatsResponse statsResponse = StatsResponse.builder()
                .avgSpend(60.0)
                .avgLiters(35.0)
                .minSpend(50.0)
                .maxSpend(70.0)
                .minLiters(30.0)
                .maxLiters(40.0)
                .spendVariance(100.0)
                .spendStdDev(10.0)
                .litersVariance(25.0)
                .litersStdDev(5.0)
                .fuelByEntry(new ArrayList<>())
                .monthlyCosts(new ArrayList<>())
                .yearlyCosts(new ArrayList<>())
                .build();

        StatsRequest statsRequest = new StatsRequest();
        statsRequest.setPeriodPreset("all");
        statsRequest.setSelectedYear(null);
        statsRequest.setSelectedMonth(null);

        when(entryService.computeStats(eq(vehicleId), any(StatsRequest.class)))
                .thenReturn(statsResponse);

        ResponseEntity<StatsResponse> response = entrtyController.getVehicleStats(vehicleId, statsRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(60.0, response.getBody().getAvgSpend());
        assertEquals(35.0, response.getBody().getAvgLiters());
        assertEquals(50.0, response.getBody().getMinSpend());
        assertEquals(70.0, response.getBody().getMaxSpend());
        assertEquals(30.0, response.getBody().getMinLiters());
        assertEquals(40.0, response.getBody().getMaxLiters());
        assertEquals(100.0, response.getBody().getSpendVariance());
        assertEquals(10.0, response.getBody().getSpendStdDev());
        assertEquals(25.0, response.getBody().getLitersVariance());
        assertEquals(5.0, response.getBody().getLitersStdDev());
        verify(entryService, times(1)).computeStats(eq(vehicleId), any(StatsRequest.class));
    }

    @Test
    void testGetVehicleStatsThisYear() {
        StatsResponse statsResponse = StatsResponse.builder()
                .avgSpend(55.0)
                .avgLiters(33.0)
                .fuelByEntry(new ArrayList<>())
                .monthlyCosts(new ArrayList<>())
                .yearlyCosts(new ArrayList<>())
                .build();

        StatsRequest statsRequest = new StatsRequest();
        statsRequest.setPeriodPreset("thisYear");
        statsRequest.setSelectedYear(null);
        statsRequest.setSelectedMonth(null);

        when(entryService.computeStats(eq(vehicleId), any(StatsRequest.class)))
                .thenReturn(statsResponse);

        ResponseEntity<StatsResponse> response = entrtyController.getVehicleStats(vehicleId, statsRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(55.0, response.getBody().getAvgSpend());
        assertEquals(33.0, response.getBody().getAvgLiters());
        verify(entryService, times(1)).computeStats(eq(vehicleId), any(StatsRequest.class));
    }

    @Test
    void testGetVehicleStatsCustom() {
        StatsResponse statsResponse = StatsResponse.builder()
                .avgSpend(65.0)
                .avgLiters(37.0)
                .fuelByEntry(new ArrayList<>())
                .monthlyCosts(new ArrayList<>())
                .yearlyCosts(new ArrayList<>())
                .build();

        StatsRequest statsRequest = new StatsRequest();
        statsRequest.setPeriodPreset("custom");
        statsRequest.setSelectedYear(2026);
        statsRequest.setSelectedMonth("04");

        when(entryService.computeStats(eq(vehicleId), any(StatsRequest.class)))
                .thenReturn(statsResponse);

        ResponseEntity<StatsResponse> response = entrtyController.getVehicleStats(vehicleId, statsRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(65.0, response.getBody().getAvgSpend());
        assertEquals(37.0, response.getBody().getAvgLiters());
        verify(entryService, times(1)).computeStats(eq(vehicleId), any(StatsRequest.class));
    }

    @Test
    void testGetVehicleStatsEmpty() {
        StatsResponse statsResponse = StatsResponse.builder()
                .fuelByEntry(new ArrayList<>())
                .monthlyCosts(new ArrayList<>())
                .yearlyCosts(new ArrayList<>())
                .build();

        StatsRequest statsRequest = new StatsRequest();
        statsRequest.setPeriodPreset("all");

        when(entryService.computeStats(eq(vehicleId), any(StatsRequest.class)))
                .thenReturn(statsResponse);

        ResponseEntity<StatsResponse> response = entrtyController.getVehicleStats(vehicleId, statsRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getAvgSpend());
        assertNull(response.getBody().getAvgLiters());
        assertTrue(response.getBody().getFuelByEntry().isEmpty());
        assertTrue(response.getBody().getMonthlyCosts().isEmpty());
        assertTrue(response.getBody().getYearlyCosts().isEmpty());
        verify(entryService, times(1)).computeStats(eq(vehicleId), any(StatsRequest.class));
    }
}



