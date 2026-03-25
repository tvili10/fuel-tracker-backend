package hu.fueltracker.service;

import hu.fueltracker.dto.CreateVehicleRequest;
import hu.fueltracker.dto.VehicleDTO;
import hu.fueltracker.entity.VehicleEntity;
import hu.fueltracker.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public VehicleDTO createVehicle(UUID userId, CreateVehicleRequest request) {
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setUserId(userId);
        vehicle.setName(request.getName());
        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setTankCapacity(request.getTankCapacity());

        VehicleEntity savedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(savedVehicle);
    }

    public VehicleDTO getVehicleById(UUID vehicleId) {
        VehicleEntity vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
        return convertToDTO(vehicle);
    }

    public List<VehicleDTO> getUserVehicles(UUID userId) {
        return vehicleRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO updateVehicle(UUID vehicleId, CreateVehicleRequest request) {
        VehicleEntity vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));

        if (request.getName() != null) vehicle.setName(request.getName());
        if (request.getMake() != null) vehicle.setMake(request.getMake());
        if (request.getModel() != null) vehicle.setModel(request.getModel());
        if (request.getLicensePlate() != null) vehicle.setLicensePlate(request.getLicensePlate());
        if (request.getFuelType() != null) vehicle.setFuelType(request.getFuelType());
        if (request.getTankCapacity() != null) vehicle.setTankCapacity(request.getTankCapacity());

        VehicleEntity updatedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(updatedVehicle);
    }

    public void deleteVehicle(UUID vehicleId) {
        VehicleEntity vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
        vehicleRepository.delete(vehicle);
    }

    private VehicleDTO convertToDTO(VehicleEntity vehicle) {
        return new VehicleDTO(
                vehicle.getId(),
                vehicle.getUserId(),
                vehicle.getName(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getLicensePlate(),
                vehicle.getFuelType(),
                vehicle.getTankCapacity()
        );
    }
}

