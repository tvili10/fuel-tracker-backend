package hu.fueltracker.repository;

import hu.fueltracker.entity.EntryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntryRepositoryTest {

    @Mock
    private EntryRepository entryRepository;

    private UUID userId;
    private UUID vehicleId;
    private EntryEntity entryEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        vehicleId = UUID.randomUUID();

        entryEntity = new EntryEntity();
        entryEntity.setUserId(userId);
        entryEntity.setVehicleId(vehicleId);
        entryEntity.setDate(LocalDateTime.now());
        entryEntity.setAmount(30.0);
        entryEntity.setCost(50.0);
    }

    @Test
    void testSaveEntry() {
        EntryEntity saved = new EntryEntity();
        saved.setId(UUID.randomUUID());
        saved.setUserId(userId);
        saved.setVehicleId(vehicleId);
        saved.setDate(LocalDateTime.now());
        saved.setAmount(30.0);
        saved.setCost(50.0);

        when(entryRepository.save(any(EntryEntity.class))).thenReturn(saved);

        EntryEntity result = entryRepository.save(entryEntity);

        assertNotNull(result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals(vehicleId, result.getVehicleId());
        assertEquals(30.0, result.getAmount());
        assertEquals(50.0, result.getCost());
        verify(entryRepository, times(1)).save(any(EntryEntity.class));
    }

    @Test
    void testFindEntryById() {
        UUID entryId = UUID.randomUUID();
        EntryEntity entry = new EntryEntity();
        entry.setId(entryId);
        entry.setUserId(userId);
        entry.setVehicleId(vehicleId);
        entry.setDate(LocalDateTime.now());
        entry.setAmount(30.0);

        when(entryRepository.findById(entryId)).thenReturn(Optional.of(entry));

        Optional<EntryEntity> found = entryRepository.findById(entryId);

        assertTrue(found.isPresent());
        assertEquals(userId, found.get().getUserId());
        assertEquals(vehicleId, found.get().getVehicleId());
        assertEquals(30.0, found.get().getAmount());
        verify(entryRepository, times(1)).findById(entryId);
    }

    @Test
    void testFindEntryByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        when(entryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<EntryEntity> found = entryRepository.findById(nonExistentId);

        assertFalse(found.isPresent());
        verify(entryRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testFindEntriesByUserId() {
        EntryEntity entry1 = new EntryEntity();
        entry1.setId(UUID.randomUUID());
        entry1.setUserId(userId);
        entry1.setVehicleId(vehicleId);
        entry1.setDate(LocalDateTime.now());
        entry1.setAmount(30.0);

        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(userId);
        entry2.setVehicleId(UUID.randomUUID());
        entry2.setDate(LocalDateTime.now());
        entry2.setAmount(25.0);

        List<EntryEntity> entries = new ArrayList<>();
        entries.add(entry1);
        entries.add(entry2);

        when(entryRepository.findByUserId(userId)).thenReturn(entries);

        List<EntryEntity> result = entryRepository.findByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(e -> e.getUserId().equals(userId)));
        verify(entryRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindEntriesByUserIdEmpty() {
        UUID anotherUserId = UUID.randomUUID();

        when(entryRepository.findByUserId(anotherUserId)).thenReturn(new ArrayList<>());

        List<EntryEntity> entries = entryRepository.findByUserId(anotherUserId);

        assertNotNull(entries);
        assertEquals(0, entries.size());
        verify(entryRepository, times(1)).findByUserId(anotherUserId);
    }

    @Test
    void testFindEntriesByVehicleId() {
        EntryEntity entry1 = new EntryEntity();
        entry1.setId(UUID.randomUUID());
        entry1.setUserId(UUID.randomUUID());
        entry1.setVehicleId(vehicleId);
        entry1.setDate(LocalDateTime.now());
        entry1.setAmount(30.0);

        EntryEntity entry2 = new EntryEntity();
        entry2.setId(UUID.randomUUID());
        entry2.setUserId(UUID.randomUUID());
        entry2.setVehicleId(vehicleId);
        entry2.setDate(LocalDateTime.now());
        entry2.setAmount(20.0);

        List<EntryEntity> entries = new ArrayList<>();
        entries.add(entry1);
        entries.add(entry2);

        when(entryRepository.findByVehicleId(vehicleId)).thenReturn(entries);

        List<EntryEntity> result = entryRepository.findByVehicleId(vehicleId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(e -> e.getVehicleId().equals(vehicleId)));
        verify(entryRepository, times(1)).findByVehicleId(vehicleId);
    }

    @Test
    void testFindEntriesByVehicleIdEmpty() {
        UUID anotherVehicleId = UUID.randomUUID();

        when(entryRepository.findByVehicleId(anotherVehicleId)).thenReturn(new ArrayList<>());

        List<EntryEntity> entries = entryRepository.findByVehicleId(anotherVehicleId);

        assertNotNull(entries);
        assertEquals(0, entries.size());
        verify(entryRepository, times(1)).findByVehicleId(anotherVehicleId);
    }

    @Test
    void testUpdateEntry() {
        UUID entryId = UUID.randomUUID();
        EntryEntity entry = new EntryEntity();
        entry.setId(entryId);
        entry.setAmount(30.0);

        EntryEntity updated = new EntryEntity();
        updated.setId(entryId);
        updated.setAmount(35.0);
        updated.setCost(60.0);

        when(entryRepository.save(any(EntryEntity.class))).thenReturn(updated);

        EntryEntity result = entryRepository.save(updated);

        assertEquals(35.0, result.getAmount());
        assertEquals(60.0, result.getCost());
        verify(entryRepository, times(1)).save(any(EntryEntity.class));
    }

    @Test
    void testDeleteEntry() {
        UUID entryId = UUID.randomUUID();
        EntryEntity entry = new EntryEntity();
        entry.setId(entryId);

        doNothing().when(entryRepository).delete(entry);
        entryRepository.delete(entry);

        verify(entryRepository, times(1)).delete(entry);
    }
}


