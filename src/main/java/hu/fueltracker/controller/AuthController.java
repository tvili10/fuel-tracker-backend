package hu.fueltracker.controller;

import hu.fueltracker.dto.LoginRequest;
import hu.fueltracker.dto.LoginResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // TODO: Implement actual login logic here
        // For now, just return a simple response
        System.out.println("Recieved login request: " + request);
        return new LoginResponse("Login endpoint received data", true);
    }
}

