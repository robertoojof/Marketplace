package com.ufpb.mps.makertplace.health;

import org.springframework.stereotype.Service;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;

@Service
@SecurityRequirements()
public class HealthService {
  public String checkStatus() {
    return "API OK!";
  }
}
