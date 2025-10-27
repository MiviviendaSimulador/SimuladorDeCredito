package com.mivivienda.simulador.simuladordecredito.simulation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimulationResponse {
    private UUID id;
    private UUID userId;
    private String params;
    private LocalDateTime createdAt;
}

