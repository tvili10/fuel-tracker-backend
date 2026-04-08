package hu.fueltracker.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleEntityTest {

    private VehicleEntity vehicleEntity;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        vehicleEntity = new VehicleEntity();
    }

    @Test
    void testEntityCreation() {
        assertNotNull(vehicleEntity);
    }

    @Test
    void testSetAndGetId() {
        UUID id = UUID.randomUUID();
        vehicleEntity.setId(id);

        assertEquals(id, vehicleEntity.getId());
    }

    @Test
    void testSetAndGetUserId() {
        vehicleEntity.setUserId(userId);

        assertEquals(userId, vehicleEntity.getUserId());
    }

    @Test
    void testSetAndGetName() {
        String name = "My Car";
        vehicleEntity.setName(name);

        assertEquals(name, vehicleEntity.getName());
    }

    @Test
    void testSetAndGetMake() {
        String make = "Toyota";
        vehicleEntity.setMake(make);

        assertEquals(make, vehicleEntity.getMake());
    }

    @Test
    void testSetAndGetModel() {
        String model = "Camry";
        vehicleEntity.setModel(model);

        assertEquals(model, vehicleEntity.getModel());
    }

    @Test
    void testSetAndGetLicensePlate() {
        String licensePlate = "ABC-123";
        vehicleEntity.setLicensePlate(licensePlate);

        assertEquals(licensePlate, vehicleEntity.getLicensePlate());
    }

    @Test
    void testSetAndGetFuelType() {
        String fuelType = "Diesel";
        vehicleEntity.setFuelType(fuelType);

        assertEquals(fuelType, vehicleEntity.getFuelType());
    }

    @Test
    void testSetAndGetTankCapacity() {
        Double tankCapacity = 60.0;
        vehicleEntity.setTankCapacity(tankCapacity);

        assertEquals(tankCapacity, vehicleEntity.getTankCapacity());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        String name = "My Car";
        String make = "Toyota";
        String model = "Camry";
        String licensePlate = "ABC-123";
        String fuelType = "Diesel";
        Double tankCapacity = 60.0;

        VehicleEntity entity = new VehicleEntity(id, userId, name, make, model, licensePlate, fuelType, tankCapacity);

        assertEquals(id, entity.getId());
        assertEquals(userId, entity.getUserId());
        assertEquals(name, entity.getName());
        assertEquals(make, entity.getMake());
        assertEquals(model, entity.getModel());
        assertEquals(licensePlate, entity.getLicensePlate());
        assertEquals(fuelType, entity.getFuelType());
        assertEquals(tankCapacity, entity.getTankCapacity());
    }

    @Test
    void testNoArgsConstructor() {
        VehicleEntity entity = new VehicleEntity();

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        VehicleEntity entity1 = new VehicleEntity();
        entity1.setId(id);
        entity1.setUserId(userId);
        entity1.setName("My Car");

        VehicleEntity entity2 = new VehicleEntity();
        entity2.setId(id);
        entity2.setUserId(userId);
        entity2.setName("My Car");

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void testOptionalFields() {
        vehicleEntity.setUserId(userId);
        vehicleEntity.setName("My Car");

        // Optional fields should be null
        assertNull(vehicleEntity.getMake());
        assertNull(vehicleEntity.getModel());
        assertNull(vehicleEntity.getLicensePlate());
        assertNull(vehicleEntity.getFuelType());
        assertNull(vehicleEntity.getTankCapacity());
    }
}

