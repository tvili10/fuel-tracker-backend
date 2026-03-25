package hu.fueltracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    private String make;

    private String model;

    @Column(name = "license_plate", unique = true)
    private String licensePlate;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "tank_capacity")
    private Double tankCapacity;


}