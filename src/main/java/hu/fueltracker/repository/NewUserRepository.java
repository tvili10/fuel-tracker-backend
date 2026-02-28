package hu.fueltracker.repository;

import hu.fueltracker.entity.NewUserEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface NewUserRepository extends JpaRepository<NewUserEntry, Long> {
    // You can add custom query methods here if needed
}
