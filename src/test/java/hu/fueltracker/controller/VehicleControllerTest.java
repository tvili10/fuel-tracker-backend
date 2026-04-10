package hu.fueltracker.controller;

import hu.fueltracker.dto.vehicle.CreateVehicleRequest;
import hu.fueltracker.dto.vehicle.VehicleDTO;
import hu.fueltracker.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private UUID userId;
    private UUID vehicleId;
    private VehicleDTO vehicleDTO;
    private CreateVehicleRequest createVehicleRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        vehicleId = UUID.randomUUID();

        createVehicleRequest = new CreateVehicleRequest();
        createVehicleRequest.setName("My Car");
        createVehicleRequest.setMake("Toyota");
        createVehicleRequest.setModel("Camry");
        createVehicleRequest.setLicensePlate("ABC-123");
        createVehicleRequest.setFuelType("Diesel");
        createVehicleRequest.setTankCapacity(60.0);

        vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicleId);
        vehicleDTO.setUserId(userId);
        vehicleDTO.setName("My Car");
        vehicleDTO.setMake("Toyota");
        vehicleDTO.setModel("Camry");
        vehicleDTO.setLicensePlate("ABC-123");
        vehicleDTO.setFuelType("Diesel");
        vehicleDTO.setTankCapacity(60.0);
    }

    @Test
    void testAddVehicle() {
        when(vehicleService.createVehicle(eq(userId), any(CreateVehicleRequest.class)))
                .thenReturn(vehicleDTO);

        ResponseEntity<VehicleDTO> response = vehicleController.addVehicle(userId, createVehicleRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("My Car", response.getBody().getName());
        assertEquals("Toyota", response.getBody().getMake());
        verify(vehicleService, times(1)).createVehicle(eq(userId), any(CreateVehicleRequest.class));
    }

    @Test
    void testDeleteVehicle() {
        doNothing().when(vehicleService).deleteVehicle(vehicleId);

        ResponseEntity<Void> response = vehicleController.deleteVehicle(vehicleId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(vehicleService, times(1)).deleteVehicle(vehicleId);
    }

    @Test
    void testListUserVehicles() {
        VehicleDTO vehicle2 = new VehicleDTO();
        vehicle2.setId(UUID.randomUUID());
        vehicle2.setUserId(userId);
        vehicle2.setName("Second Car");
        vehicle2.setMake("Honda");
        vehicle2.setModel("Civic");

        List<VehicleDTO> vehicles = Arrays.asList(vehicleDTO, vehicle2);
        when(vehicleService.getUserVehicles(userId)).thenReturn(vehicles);

        ResponseEntity<List<VehicleDTO>> response = vehicleController.listUserVehicles(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("My Car", response.getBody().get(0).getName());
        assertEquals("Second Car", response.getBody().get(1).getName());
        verify(vehicleService, times(1)).getUserVehicles(userId);
    }

    @Test
    void testListUserVehiclesEmpty() {
        List<VehicleDTO> vehicles = Arrays.asList();
        when(vehicleService.getUserVehicles(userId)).thenReturn(vehicles);

        ResponseEntity<List<VehicleDTO>> response = vehicleController.listUserVehicles(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(vehicleService, times(1)).getUserVehicles(userId);
    }
}



