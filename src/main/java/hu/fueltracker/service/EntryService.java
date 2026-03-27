package hu.fueltracker.service;

import hu.fueltracker.dto.CreateEntryRequest;
import hu.fueltracker.dto.EntryDTO;
import hu.fueltracker.entity.EntryEntity;
import hu.fueltracker.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EntryService {

    @Autowired
    private EntryRepository entryRepository;

    public EntryDTO createEntry(UUID userId, CreateEntryRequest request) {
        EntryEntity entry = new EntryEntity();
        entry.setUserId(userId);
        entry.setVehicleId(request.getVehicleId());
        entry.setAmount(request.getAmount());
        entry.setFuelQuantity(request.getFuelQuantity());
        entry.setEntryDate(request.getEntryDate());
        entry.setOdometerReading(request.getOdometerReading());
        entry.setNotes(request.getNotes());

        EntryEntity savedEntry = entryRepository.save(entry);
        return convertToDTO(savedEntry);
    }

    public EntryDTO getEntryById(UUID entryId) {
        EntryEntity entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found with id: " + entryId));
        return convertToDTO(entry);
    }

    public List<EntryDTO> getUserEntries(UUID userId) {
        return entryRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EntryDTO> getVehicleEntries(UUID vehicleId) {
        return entryRepository.findByVehicleId(vehicleId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EntryDTO updateEntry(UUID entryId, CreateEntryRequest request) {
        EntryEntity entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found with id: " + entryId));

        if (request.getVehicleId() != null) entry.setVehicleId(request.getVehicleId());
        if (request.getAmount() != null) entry.setAmount(request.getAmount());
        if (request.getFuelQuantity() != null) entry.setFuelQuantity(request.getFuelQuantity());
        if (request.getEntryDate() != null) entry.setEntryDate(request.getEntryDate());
        if (request.getOdometerReading() != null) entry.setOdometerReading(request.getOdometerReading());
        if (request.getNotes() != null) entry.setNotes(request.getNotes());

        EntryEntity updatedEntry = entryRepository.save(entry);
        return convertToDTO(updatedEntry);
    }

    public void deleteEntry(UUID entryId) {
        EntryEntity entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found with id: " + entryId));
        entryRepository.delete(entry);
    }

    private EntryDTO convertToDTO(EntryEntity entry) {
        return new EntryDTO(
                entry.getId(),
                entry.getUserId(),
                entry.getVehicleId(),
                entry.getAmount(),
                entry.getFuelQuantity(),
                entry.getEntryDate(),
                entry.getOdometerReading(),
                entry.getNotes(),
                entry.getCreatedAt()
        );
    }
}

