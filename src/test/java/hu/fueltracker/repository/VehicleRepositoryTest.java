package hu.fueltracker.repository;

import hu.fueltracker.entity.VehicleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleRepositoryTest {

    @Mock
    private VehicleRepository vehicleRepository;

    private UUID userId;
    private VehicleEntity vehicleEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        vehicleEntity = new VehicleEntity();
        vehicleEntity.setUserId(userId);
        vehicleEntity.setName("Test Car");
        vehicleEntity.setMake("Toyota");
        vehicleEntity.setModel("Camry");
        vehicleEntity.setLicensePlate("ABC-123");
        vehicleEntity.setFuelType("Diesel");
        vehicleEntity.setTankCapacity(60.0);
    }

    @Test
    void testSaveVehicle() {
        VehicleEntity saved = new VehicleEntity();
        saved.setId(UUID.randomUUID());
        saved.setUserId(userId);
        saved.setName("Test Car");
        saved.setMake("Toyota");

        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(saved);

        VehicleEntity result = vehicleRepository.save(vehicleEntity);

        assertNotNull(result.getId());
        assertEquals("Test Car", result.getName());
        assertEquals(userId, result.getUserId());
        verify(vehicleRepository, times(1)).save(any(VehicleEntity.class));
    }

    @Test
    void testFindVehicleById() {
        UUID vehicleId = UUID.randomUUID();
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setId(vehicleId);
        vehicle.setName("Test Car");
        vehicle.setMake("Toyota");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        Optional<VehicleEntity> found = vehicleRepository.findById(vehicleId);

        assertTrue(found.isPresent());
        assertEquals("Test Car", found.get().getName());
        assertEquals("Toyota", found.get().getMake());
        verify(vehicleRepository, times(1)).findById(vehicleId);
    }

    @Test
    void testFindVehicleByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        when(vehicleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<VehicleEntity> found = vehicleRepository.findById(nonExistentId);

        assertFalse(found.isPresent());
        verify(vehicleRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testFindVehiclesByUserId() {
        VehicleEntity vehicle1 = new VehicleEntity();
        vehicle1.setId(UUID.randomUUID());
        vehicle1.setUserId(userId);
        vehicle1.setName("Test Car");
        vehicle1.setMake("Toyota");

        VehicleEntity vehicle2 = new VehicleEntity();
        vehicle2.setId(UUID.randomUUID());
        vehicle2.setUserId(userId);
        vehicle2.setName("Second Car");
        vehicle2.setMake("Honda");

        List<VehicleEntity> vehicles = new ArrayList<>();
        vehicles.add(vehicle1);
        vehicles.add(vehicle2);

        when(vehicleRepository.findByUserId(userId)).thenReturn(vehicles);

        List<VehicleEntity> result = vehicleRepository.findByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(v -> v.getName().equals("Test Car")));
        assertTrue(result.stream().anyMatch(v -> v.getName().equals("Second Car")));
        verify(vehicleRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindVehiclesByUserIdEmpty() {
        UUID anotherUserId = UUID.randomUUID();

        when(vehicleRepository.findByUserId(anotherUserId)).thenReturn(new ArrayList<>());

        List<VehicleEntity> vehicles = vehicleRepository.findByUserId(anotherUserId);

        assertNotNull(vehicles);
        assertEquals(0, vehicles.size());
        verify(vehicleRepository, times(1)).findByUserId(anotherUserId);
    }

    @Test
    void testUpdateVehicle() {
        UUID vehicleId = UUID.randomUUID();
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setId(vehicleId);
        vehicle.setName("Old Name");
        vehicle.setMake("Toyota");

        VehicleEntity updated = new VehicleEntity();
        updated.setId(vehicleId);
        updated.setName("Updated Car");
        updated.setMake("Honda");

        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(updated);

        VehicleEntity result = vehicleRepository.save(updated);

        assertEquals("Updated Car", result.getName());
        assertEquals("Honda", result.getMake());
        verify(vehicleRepository, times(1)).save(any(VehicleEntity.class));
    }

    @Test
    void testDeleteVehicle() {
        UUID vehicleId = UUID.randomUUID();
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setId(vehicleId);

        doNothing().when(vehicleRepository).delete(vehicle);
        vehicleRepository.delete(vehicle);

        verify(vehicleRepository, times(1)).delete(vehicle);
    }
}


