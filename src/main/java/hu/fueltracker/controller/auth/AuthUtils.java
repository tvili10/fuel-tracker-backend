package hu.fueltracker.controller.auth;

import hu.fueltracker.dto.register.RegisterRequest;
import hu.fueltracker.entity.NewUserEntry;

public class AuthUtils {


    public static NewUserEntry mapToNewUserEntry(RegisterRequest request) {
        NewUserEntry newUserEntry = new NewUserEntry();
        newUserEntry.setEmail(request.getEmail());
        newUserEntry.setUsername(request.getUsername());
        newUserEntry.setPassword(request.getPassword());
        return newUserEntry;
    }
}
