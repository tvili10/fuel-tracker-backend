package hu.fueltracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
@RestController
public class PingController {

    private static final Logger LOG = Logger.getLogger(PingController.class.getName());

    @GetMapping("/ping")
    public ResponseEntity<String> receivePing() {
        LOG.info("ping");
        return ResponseEntity.ok("pong");

    }
}