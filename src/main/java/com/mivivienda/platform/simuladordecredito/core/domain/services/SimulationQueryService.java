package com.mivivienda.platform.simuladordecredito.core.domain.services;

import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.Simulation;
import com.mivivienda.platform.simuladordecredito.core.domain.model.queries.GetAllSimulationsQuery;
import com.mivivienda.platform.simuladordecredito.core.domain.model.queries.GetSimulationByIdQuery;

import java.util.List;
import java.util.Optional;

public interface SimulationQueryService {
    List<Simulation> handle(GetAllSimulationsQuery query);
    
    Optional<Simulation> handle(GetSimulationByIdQuery query);
}