package hu.fueltracker.service;

import hu.fueltracker.dto.entry.CreateEntryRequest;
import hu.fueltracker.dto.entry.EntryDTO;
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
}

