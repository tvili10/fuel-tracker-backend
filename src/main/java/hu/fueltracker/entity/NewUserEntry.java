package hu.fueltracker.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * This entity represents a new user entry in the system. It is used to store information about users who have registered but have not yet completed the full registration process.
 *
 **/


@Entity
@Table(name = "\"user-information\"")
@Data
public class NewUserEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String password;

}
