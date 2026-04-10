package hu.fueltracker.dto.vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleDTOTest {

    private VehicleDTO vehicleDTO;
    private UUID id;
    private UUID userId;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        userId = UUID.randomUUID();
        vehicleDTO = new VehicleDTO();
    }

    @Test
    void testEntityCreation() {
        assertNotNull(vehicleDTO);
    }

    @Test
    void testSetAndGetId() {
        vehicleDTO.setId(id);

        assertEquals(id, vehicleDTO.getId());
    }

    @Test
    void testSetAndGetUserId() {
        vehicleDTO.setUserId(userId);

        assertEquals(userId, vehicleDTO.getUserId());
    }

    @Test
    void testSetAndGetName() {
        String name = "My Car";
        vehicleDTO.setName(name);

        assertEquals(name, vehicleDTO.getName());
    }

    @Test
    void testSetAndGetMake() {
        String make = "Toyota";
        vehicleDTO.setMake(make);

        assertEquals(make, vehicleDTO.getMake());
    }

    @Test
    void testSetAndGetModel() {
        String model = "Camry";
        vehicleDTO.setModel(model);

        assertEquals(model, vehicleDTO.getModel());
    }

    @Test
    void testSetAndGetLicensePlate() {
        String licensePlate = "ABC-123";
        vehicleDTO.setLicensePlate(licensePlate);

        assertEquals(licensePlate, vehicleDTO.getLicensePlate());
    }

    @Test
    void testSetAndGetFuelType() {
        String fuelType = "Diesel";
        vehicleDTO.setFuelType(fuelType);

        assertEquals(fuelType, vehicleDTO.getFuelType());
    }

    @Test
    void testSetAndGetTankCapacity() {
        Double tankCapacity = 60.0;
        vehicleDTO.setTankCapacity(tankCapacity);

        assertEquals(tankCapacity, vehicleDTO.getTankCapacity());
    }

    @Test
    void testAllArgsConstructor() {
        String name = "My Car";
        String make = "Toyota";
        String model = "Camry";
        String licensePlate = "ABC-123";
        String fuelType = "Diesel";
        Double tankCapacity = 60.0;

        VehicleDTO dto = new VehicleDTO(id, userId, name, make, model, licensePlate, fuelType, tankCapacity);

        assertEquals(id, dto.getId());
        assertEquals(userId, dto.getUserId());
        assertEquals(name, dto.getName());
        assertEquals(make, dto.getMake());
        assertEquals(model, dto.getModel());
        assertEquals(licensePlate, dto.getLicensePlate());
        assertEquals(fuelType, dto.getFuelType());
        assertEquals(tankCapacity, dto.getTankCapacity());
    }

    @Test
    void testNoArgsConstructor() {
        VehicleDTO dto = new VehicleDTO();

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        VehicleDTO dto1 = new VehicleDTO(id, userId, "My Car", "Toyota", "Camry", "ABC-123", "Diesel", 60.0);
        VehicleDTO dto2 = new VehicleDTO(id, userId, "My Car", "Toyota", "Camry", "ABC-123", "Diesel", 60.0);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        vehicleDTO.setId(id);
        vehicleDTO.setName("My Car");

        String toString = vehicleDTO.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("My Car"));
    }
}

