package com.mivivienda.platform.simuladordecredito.core.domain.services;

import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.Simulation;
import com.mivivienda.platform.simuladordecredito.core.domain.model.commands.CreateSimulationCommand;

import java.util.Optional;

public interface SimulationCommandService {
    Optional<Simulation> handle(CreateSimulationCommand command);
}
