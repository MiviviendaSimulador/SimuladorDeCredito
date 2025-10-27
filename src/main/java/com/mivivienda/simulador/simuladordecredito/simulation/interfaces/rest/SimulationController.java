package com.mivivienda.simulador.simuladordecredito.simulation.interfaces.rest;

import com.mivivienda.simulador.simuladordecredito.auth.domain.model.User;
import com.mivivienda.simulador.simuladordecredito.simulation.application.internal.SimulationService;
import com.mivivienda.simulador.simuladordecredito.simulation.dto.SimulationRequest;
import com.mivivienda.simulador.simuladordecredito.simulation.dto.SimulationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/simulations")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping
    public ResponseEntity<SimulationResponse> createSimulation(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SimulationRequest request) {
        SimulationResponse response = simulationService.createSimulation(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SimulationResponse>> getSimulations(
            @AuthenticationPrincipal User user) {
        List<SimulationResponse> simulations = simulationService.getSimulations(user.getId());
        return ResponseEntity.ok(simulations);
    }
}

