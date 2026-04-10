package hu.fueltracker.repository;

import hu.fueltracker.entity.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, UUID> {
    List<EntryEntity> findByUserId(UUID userId);
    List<EntryEntity> findByVehicleId(UUID vehicleId);

    @Query("SELECT e FROM EntryEntity e WHERE e.vehicleId = :vehicleId AND e.date >= :startDate AND e.date < :endDate")
    List<EntryEntity> findByVehicleIdAndDateRange(@Param("vehicleId") UUID vehicleId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);
}

