package hu.fueltracker.dto.entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateEntryRequestTest {

    private CreateEntryRequest request;
    private UUID vehicleId;

    @BeforeEach
    void setUp() {
        vehicleId = UUID.randomUUID();
        request = new CreateEntryRequest();
    }

    @Test
    void testRequestCreation() {
        assertNotNull(request);
    }

    @Test
    void testSetAndGetVehicleId() {
        request.setVehicleId(vehicleId);

        assertEquals(vehicleId, request.getVehicleId());
    }

    @Test
    void testSetAndGetAmount() {
        Double amount = 50.0;
        request.setAmount(amount);

        assertEquals(amount, request.getAmount());
    }

    @Test
    void testSetAndGetFuelQuantity() {
        Double fuelQuantity = 30.0;
        request.setFuelQuantity(fuelQuantity);

        assertEquals(fuelQuantity, request.getFuelQuantity());
    }

    @Test
    void testSetAndGetEntryDate() {
        LocalDateTime date = LocalDateTime.now();
        request.setEntryDate(date);

        assertEquals(date, request.getEntryDate());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime date = LocalDateTime.now();
        Double amount = 50.0;
        Double fuelQuantity = 30.0;

        CreateEntryRequest req = new CreateEntryRequest(vehicleId, amount, fuelQuantity, date);

        assertEquals(vehicleId, req.getVehicleId());
        assertEquals(amount, req.getAmount());
        assertEquals(fuelQuantity, req.getFuelQuantity());
        assertEquals(date, req.getEntryDate());
    }

    @Test
    void testNoArgsConstructor() {
        CreateEntryRequest req = new CreateEntryRequest();

        assertNotNull(req);
        assertNull(req.getVehicleId());
        assertNull(req.getAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime date = LocalDateTime.now();
        CreateEntryRequest req1 = new CreateEntryRequest(vehicleId, 50.0, 30.0, date);
        CreateEntryRequest req2 = new CreateEntryRequest(vehicleId, 50.0, 30.0, date);

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testPartialConstruction() {
        request.setVehicleId(vehicleId);
        request.setAmount(50.0);

        assertEquals(vehicleId, request.getVehicleId());
        assertEquals(50.0, request.getAmount());
        assertNull(request.getFuelQuantity());
        assertNull(request.getEntryDate());
    }
}

