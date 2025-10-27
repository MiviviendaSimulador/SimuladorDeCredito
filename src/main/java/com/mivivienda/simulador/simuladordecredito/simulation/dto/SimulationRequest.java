package com.mivivienda.simulador.simuladordecredito.simulation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimulationRequest {
    @NotBlank(message = "Params are required")
    private String params;
}

