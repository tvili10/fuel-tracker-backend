package hu.fueltracker.dto.vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateVehicleRequestTest {

    private CreateVehicleRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateVehicleRequest();
    }

    @Test
    void testRequestCreation() {
        assertNotNull(request);
    }

    @Test
    void testSetAndGetName() {
        String name = "My Car";
        request.setName(name);

        assertEquals(name, request.getName());
    }

    @Test
    void testSetAndGetMake() {
        String make = "Toyota";
        request.setMake(make);

        assertEquals(make, request.getMake());
    }

    @Test
    void testSetAndGetModel() {
        String model = "Camry";
        request.setModel(model);

        assertEquals(model, request.getModel());
    }

    @Test
    void testSetAndGetLicensePlate() {
        String licensePlate = "ABC-123";
        request.setLicensePlate(licensePlate);

        assertEquals(licensePlate, request.getLicensePlate());
    }

    @Test
    void testSetAndGetFuelType() {
        String fuelType = "Diesel";
        request.setFuelType(fuelType);

        assertEquals(fuelType, request.getFuelType());
    }

    @Test
    void testSetAndGetTankCapacity() {
        Double tankCapacity = 60.0;
        request.setTankCapacity(tankCapacity);

        assertEquals(tankCapacity, request.getTankCapacity());
    }

    @Test
    void testAllArgsConstructor() {
        String name = "My Car";
        String make = "Toyota";
        String model = "Camry";
        String licensePlate = "ABC-123";
        String fuelType = "Diesel";
        Double tankCapacity = 60.0;

        CreateVehicleRequest req = new CreateVehicleRequest(name, make, model, licensePlate, fuelType, tankCapacity);

        assertEquals(name, req.getName());
        assertEquals(make, req.getMake());
        assertEquals(model, req.getModel());
        assertEquals(licensePlate, req.getLicensePlate());
        assertEquals(fuelType, req.getFuelType());
        assertEquals(tankCapacity, req.getTankCapacity());
    }

    @Test
    void testNoArgsConstructor() {
        CreateVehicleRequest req = new CreateVehicleRequest();

        assertNotNull(req);
        assertNull(req.getName());
        assertNull(req.getMake());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateVehicleRequest req1 = new CreateVehicleRequest("My Car", "Toyota", "Camry", "ABC-123", "Diesel", 60.0);
        CreateVehicleRequest req2 = new CreateVehicleRequest("My Car", "Toyota", "Camry", "ABC-123", "Diesel", 60.0);

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testPartialConstruction() {
        request.setName("My Car");
        request.setMake("Toyota");

        assertEquals("My Car", request.getName());
        assertEquals("Toyota", request.getMake());
        assertNull(request.getModel());
        assertNull(request.getLicensePlate());
    }
}

