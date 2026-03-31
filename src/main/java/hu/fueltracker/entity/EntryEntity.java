package hu.fueltracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "fuel_amount")
    private Double amount;

    @Column(name = "cost")
    private Double cost;
}

