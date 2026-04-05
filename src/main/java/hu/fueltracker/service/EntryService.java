package hu.fueltracker.service;

import hu.fueltracker.dto.entry.CreateEntryRequest;
import hu.fueltracker.dto.entry.EntryDTO;
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
        entry.setAmount(request.getFuelQuantity());
        entry.setCost(request.getAmount());
        entry.setDate(request.getEntryDate());


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
        if (request.getFuelQuantity() != null) entry.setAmount(request.getFuelQuantity());
        if (request.getEntryDate() != null) entry.setDate(request.getEntryDate());

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
                java.sql.Timestamp.valueOf(entry.getDate()),
                entry.getAmount(),
                entry.getCost()
        );
    }
}

