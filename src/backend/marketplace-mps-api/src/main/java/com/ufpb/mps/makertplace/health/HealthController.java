package com.ufpb.mps.makertplace.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final HealthService healthService;

    // Injeção de dependência via construtor (Boa prática)
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping
    public String getHealth() {
        return healthService.checkStatus();
    }
}