package hu.fueltracker.service;

import hu.fueltracker.dto.vehicle.CreateVehicleRequest;
import hu.fueltracker.dto.vehicle.VehicleDTO;
import hu.fueltracker.entity.VehicleEntity;
import hu.fueltracker.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private UUID userId;
    private UUID vehicleId;
    private VehicleEntity vehicleEntity;
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

        vehicleEntity = new VehicleEntity();
        vehicleEntity.setId(vehicleId);
        vehicleEntity.setUserId(userId);
        vehicleEntity.setName("My Car");
        vehicleEntity.setMake("Toyota");
        vehicleEntity.setModel("Camry");
        vehicleEntity.setLicensePlate("ABC-123");
        vehicleEntity.setFuelType("Diesel");
        vehicleEntity.setTankCapacity(60.0);
    }

    @Test
    void testCreateVehicle() {
        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(vehicleEntity);

        VehicleDTO result = vehicleService.createVehicle(userId, createVehicleRequest);

        assertNotNull(result);
        assertEquals("My Car", result.getName());
        assertEquals("Toyota", result.getMake());
        assertEquals(userId, result.getUserId());
        verify(vehicleRepository, times(1)).save(any(VehicleEntity.class));
    }

    @Test
    void testGetVehicleById() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicleEntity));

        VehicleDTO result = vehicleService.getVehicleById(vehicleId);

        assertNotNull(result);
        assertEquals(vehicleId, result.getId());
        assertEquals("My Car", result.getName());
        verify(vehicleRepository, times(1)).findById(vehicleId);
    }

    @Test
    void testGetVehicleByIdNotFound() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> vehicleService.getVehicleById(vehicleId));
        verify(vehicleRepository, times(1)).findById(vehicleId);
    }

    @Test
    void testGetUserVehicles() {
        VehicleEntity vehicle2 = new VehicleEntity();
        vehicle2.setId(UUID.randomUUID());
        vehicle2.setUserId(userId);
        vehicle2.setName("Second Car");
        vehicle2.setMake("Honda");

        when(vehicleRepository.findByUserId(userId)).thenReturn(Arrays.asList(vehicleEntity, vehicle2));

        List<VehicleDTO> results = vehicleService.getUserVehicles(userId);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("My Car", results.get(0).getName());
        assertEquals("Second Car", results.get(1).getName());
        verify(vehicleRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetUserVehiclesEmpty() {
        when(vehicleRepository.findByUserId(userId)).thenReturn(Arrays.asList());

        List<VehicleDTO> results = vehicleService.getUserVehicles(userId);

        assertNotNull(results);
        assertEquals(0, results.size());
        verify(vehicleRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testUpdateVehicle() {
        CreateVehicleRequest updateRequest = new CreateVehicleRequest();
        updateRequest.setName("Updated Car");
        updateRequest.setMake("Honda");
        updateRequest.setModel("Accord");
        updateRequest.setLicensePlate("XYZ-789");
        updateRequest.setFuelType("Petrol");
        updateRequest.setTankCapacity(70.0);

        VehicleEntity updatedEntity = new VehicleEntity();
        updatedEntity.setId(vehicleId);
        updatedEntity.setUserId(userId);
        updatedEntity.setName("Updated Car");
        updatedEntity.setMake("Honda");
        updatedEntity.setModel("Accord");
        updatedEntity.setLicensePlate("XYZ-789");
        updatedEntity.setFuelType("Petrol");
        updatedEntity.setTankCapacity(70.0);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicleEntity));
        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(updatedEntity);

        VehicleDTO result = vehicleService.updateVehicle(vehicleId, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Car", result.getName());
        assertEquals("Honda", result.getMake());
        assertEquals("XYZ-789", result.getLicensePlate());
        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(any(VehicleEntity.class));
    }

    @Test
    void testUpdateVehiclePartial() {
        CreateVehicleRequest updateRequest = new CreateVehicleRequest();
        updateRequest.setName("Updated Car");
        // Other fields are null

        VehicleEntity updatedEntity = new VehicleEntity();
        updatedEntity.setId(vehicleId);
        updatedEntity.setUserId(userId);
        updatedEntity.setName("Updated Car");
        updatedEntity.setMake("Toyota");
        updatedEntity.setModel("Camry");
        updatedEntity.setLicensePlate("ABC-123");
        updatedEntity.setFuelType("Diesel");
        updatedEntity.setTankCapacity(60.0);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicleEntity));
        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(updatedEntity);

        VehicleDTO result = vehicleService.updateVehicle(vehicleId, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Car", result.getName());
        assertEquals("Toyota", result.getMake());
        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).save(any(VehicleEntity.class));
    }

    @Test
    void testUpdateVehicleNotFound() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> vehicleService.updateVehicle(vehicleId, createVehicleRequest));
        verify(vehicleRepository, times(1)).findById(vehicleId);
    }

    @Test
    void testDeleteVehicle() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicleEntity));
        doNothing().when(vehicleRepository).delete(vehicleEntity);

        vehicleService.deleteVehicle(vehicleId);

        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).delete(vehicleEntity);
    }

    @Test
    void testDeleteVehicleNotFound() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> vehicleService.deleteVehicle(vehicleId));
        verify(vehicleRepository, times(1)).findById(vehicleId);
    }
}

