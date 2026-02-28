package hu.fueltracker.controller;

import hu.fueltracker.dto.login.LoginRequest;
import hu.fueltracker.dto.login.LoginResponse;
import hu.fueltracker.dto.register.RegisterRequest;
import hu.fueltracker.dto.register.RegisterResponse;
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

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        System.out.println("Recieved register request: " + request);
        return new RegisterResponse("Register endpoint received data", true);
    }
}

