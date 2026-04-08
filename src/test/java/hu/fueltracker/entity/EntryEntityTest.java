package hu.fueltracker.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EntryEntityTest {

    private EntryEntity entryEntity;
    private UUID userId;
    private UUID vehicleId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        vehicleId = UUID.randomUUID();

        entryEntity = new EntryEntity();
    }

    @Test
    void testEntityCreation() {
        assertNotNull(entryEntity);
    }

    @Test
    void testSetAndGetId() {
        UUID id = UUID.randomUUID();
        entryEntity.setId(id);

        assertEquals(id, entryEntity.getId());
    }

    @Test
    void testSetAndGetUserId() {
        entryEntity.setUserId(userId);

        assertEquals(userId, entryEntity.getUserId());
    }

    @Test
    void testSetAndGetVehicleId() {
        entryEntity.setVehicleId(vehicleId);

        assertEquals(vehicleId, entryEntity.getVehicleId());
    }

    @Test
    void testSetAndGetDate() {
        LocalDateTime now = LocalDateTime.now();
        entryEntity.setDate(now);

        assertEquals(now, entryEntity.getDate());
    }

    @Test
    void testSetAndGetAmount() {
        Double amount = 30.5;
        entryEntity.setAmount(amount);

        assertEquals(amount, entryEntity.getAmount());
    }

    @Test
    void testSetAndGetCost() {
        Double cost = 50.75;
        entryEntity.setCost(cost);

        assertEquals(cost, entryEntity.getCost());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now();
        Double amount = 30.5;
        Double cost = 50.75;

        EntryEntity entity = new EntryEntity(id, userId, vehicleId, date, amount, cost);

        assertEquals(id, entity.getId());
        assertEquals(userId, entity.getUserId());
        assertEquals(vehicleId, entity.getVehicleId());
        assertEquals(date, entity.getDate());
        assertEquals(amount, entity.getAmount());
        assertEquals(cost, entity.getCost());
    }

    @Test
    void testNoArgsConstructor() {
        EntryEntity entity = new EntryEntity();

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        EntryEntity entity1 = new EntryEntity();
        entity1.setId(id);
        entity1.setUserId(userId);

        EntryEntity entity2 = new EntryEntity();
        entity2.setId(id);
        entity2.setUserId(userId);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }
}

