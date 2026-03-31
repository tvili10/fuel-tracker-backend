package hu.fueltracker.repository;

import hu.fueltracker.entity.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, UUID> {
    List<EntryEntity> findByUserId(UUID userId);
    List<EntryEntity> findByVehicleId(UUID vehicleId);
}

