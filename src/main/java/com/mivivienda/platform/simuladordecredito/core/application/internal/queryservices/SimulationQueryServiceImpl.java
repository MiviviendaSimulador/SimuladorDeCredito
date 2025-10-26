package com.mivivienda.platform.simuladordecredito.core.application.internal.queryservices;

import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.Simulation;
import com.mivivienda.platform.simuladordecredito.core.domain.model.queries.GetAllSimulationsQuery;
import com.mivivienda.platform.simuladordecredito.core.domain.model.queries.GetSimulationByIdQuery;
import com.mivivienda.platform.simuladordecredito.core.domain.services.SimulationQueryService;
import com.mivivienda.platform.simuladordecredito.core.infrastructure.persistence.jpa.repositories.SimulationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SimulationQueryServiceImpl implements SimulationQueryService {
    
    private final SimulationRepository simulationRepository;
    
    public SimulationQueryServiceImpl(SimulationRepository simulationRepository) {
        this.simulationRepository = simulationRepository;
    }
    
    @Override
    public List<Simulation> handle(GetAllSimulationsQuery query) {
        return simulationRepository.findAll();
    }
    
    @Override
    public Optional<Simulation> handle(GetSimulationByIdQuery query) {
        return simulationRepository.findById(query.id());
    }
}