package hu.fueltracker.controller;

import hu.fueltracker.dto.entry.CreateEntryRequest;
import hu.fueltracker.dto.entry.EntryDTO;
import hu.fueltracker.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/entries")
public class EnrtyController {

    @Autowired
    private EntryService entryService;

    private Logger LOG = Logger.getLogger(EnrtyController.class.getName());

    @PostMapping
    public ResponseEntity<EntryDTO> addEntry(@RequestHeader("User-Id") UUID userId, @RequestBody CreateEntryRequest request) {
        EntryDTO entry = entryService.createEntry(userId, request);
        LOG.info("New entry added for user " + userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(entry);
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable UUID entryId) {
        entryService.deleteEntry(entryId);
        LOG.info("Entry " + entryId + " deleted");
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EntryDTO>> listUserEntries(@RequestHeader("User-Id") UUID userId) {
        List<EntryDTO> entries = entryService.getUserEntries(userId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<EntryDTO>> listVehicleEntries(@PathVariable UUID vehicleId) {
        List<EntryDTO> entries = entryService.getVehicleEntries(vehicleId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<EntryDTO> getEntry(@PathVariable UUID entryId) {
        EntryDTO entry = entryService.getEntryById(entryId);
        return ResponseEntity.ok(entry);
    }

    @PutMapping("/{entryId}")
    public ResponseEntity<EntryDTO> updateEntry(@PathVariable UUID entryId, @RequestBody CreateEntryRequest request) {
        EntryDTO entry = entryService.updateEntry(entryId, request);
        LOG.info("Entry " + entryId + " updated");
        return ResponseEntity.ok(entry);
    }
}
