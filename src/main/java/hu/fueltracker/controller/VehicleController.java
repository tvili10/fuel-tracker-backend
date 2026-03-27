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
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    private Logger LOG = Logger.getLogger(VehicleController.class.getName());

    @PostMapping
    public ResponseEntity<VehicleDTO> addVehicle( @RequestHeader("User-Id") UUID userId, @RequestBody CreateVehicleRequest request) {
        VehicleDTO vehicle = vehicleService.createVehicle(userId, request);
        LOG.info("New vehicle added for user " + userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        LOG.info("Vehicle " + vehicleId + " deleted");
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> listUserVehicles(@RequestHeader("User-Id") UUID userId) {
        List<VehicleDTO> vehicles = vehicleService.getUserVehicles(userId);
        return ResponseEntity.ok(vehicles);
    }
}
