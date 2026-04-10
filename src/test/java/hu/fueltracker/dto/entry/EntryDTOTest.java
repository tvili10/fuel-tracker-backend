package hu.fueltracker.dto.entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EntryDTOTest {

    private EntryDTO entryDTO;
    private UUID id;
    private UUID userId;
    private UUID vehicleId;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        userId = UUID.randomUUID();
        vehicleId = UUID.randomUUID();
        entryDTO = new EntryDTO();
    }

    @Test
    void testEntityCreation() {
        assertNotNull(entryDTO);
    }

    @Test
    void testSetAndGetId() {
        entryDTO.setId(id);

        assertEquals(id, entryDTO.getId());
    }

    @Test
    void testSetAndGetUserId() {
        entryDTO.setUserId(userId);

        assertEquals(userId, entryDTO.getUserId());
    }

    @Test
    void testSetAndGetVehicleId() {
        entryDTO.setVehicleId(vehicleId);

        assertEquals(vehicleId, entryDTO.getVehicleId());
    }

    @Test
    void testSetAndGetDate() {
        java.util.Date date = new java.util.Date();
        entryDTO.setDate(date);

        assertEquals(date, entryDTO.getDate());
    }

    @Test
    void testSetAndGetAmount() {
        Double amount = 30.5;
        entryDTO.setAmount(amount);

        assertEquals(amount, entryDTO.getAmount());
    }

    @Test
    void testSetAndGetCost() {
        Double cost = 50.75;
        entryDTO.setCost(cost);

        assertEquals(cost, entryDTO.getCost());
    }

    @Test
    void testAllArgsConstructor() {
        java.util.Date date = new java.util.Date();
        Double amount = 30.5;
        Double cost = 50.75;

        EntryDTO dto = new EntryDTO(id, userId, vehicleId, date, amount, cost);

        assertEquals(id, dto.getId());
        assertEquals(userId, dto.getUserId());
        assertEquals(vehicleId, dto.getVehicleId());
        assertEquals(date, dto.getDate());
        assertEquals(amount, dto.getAmount());
        assertEquals(cost, dto.getCost());
    }

    @Test
    void testNoArgsConstructor() {
        EntryDTO dto = new EntryDTO();

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        java.util.Date date = new java.util.Date();
        EntryDTO dto1 = new EntryDTO(id, userId, vehicleId, date, 30.5, 50.75);
        EntryDTO dto2 = new EntryDTO(id, userId, vehicleId, date, 30.5, 50.75);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        entryDTO.setId(id);
        entryDTO.setAmount(30.5);

        String toString = entryDTO.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("30.5"));
    }
}

