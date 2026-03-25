package hu.fueltracker.controller;

import hu.fueltracker.dto.CreateVehicleRequest;
import hu.fueltracker.dto.VehicleDTO;
import hu.fueltracker.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleDTO> addVehicle( @RequestHeader("User-Id") UUID userId, @RequestBody CreateVehicleRequest request) {
        VehicleDTO vehicle = vehicleService.createVehicle(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> listUserVehicles(@RequestHeader("User-Id") UUID userId) {
        List<VehicleDTO> vehicles = vehicleService.getUserVehicles(userId);
        return ResponseEntity.ok(vehicles);
    }
}
