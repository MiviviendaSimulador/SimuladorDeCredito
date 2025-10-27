package com.mivivienda.simulador.simuladordecredito.simulation.application.internal;

import com.mivivienda.simulador.simuladordecredito.simulation.domain.model.Simulation;
import com.mivivienda.simulador.simuladordecredito.simulation.domain.persistence.SimulationRepository;
import com.mivivienda.simulador.simuladordecredito.simulation.dto.SimulationRequest;
import com.mivivienda.simulador.simuladordecredito.simulation.dto.SimulationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final SimulationRepository simulationRepository;

    public SimulationResponse createSimulation(UUID userId, SimulationRequest request) {
        var simulation = Simulation.builder()
                .userId(userId)
                .params(request.getParams())
                .build();

        simulationRepository.save(simulation);

        return SimulationResponse.builder()
                .id(simulation.getId())
                .userId(simulation.getUserId())
                .params(simulation.getParams())
                .createdAt(simulation.getCreatedAt())
                .build();
    }

    public List<SimulationResponse> getSimulations(UUID userId) {
        return simulationRepository.findByUserId(userId).stream()
                .map(simulation -> SimulationResponse.builder()
                        .id(simulation.getId())
                        .userId(simulation.getUserId())
                        .params(simulation.getParams())
                        .createdAt(simulation.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}

