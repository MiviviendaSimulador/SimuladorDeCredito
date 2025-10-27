package com.mivivienda.platform.simuladordecredito.core.application.internal.queryservices;

import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.Simulation;
import com.mivivienda.platform.simuladordecredito.core.infrastructure.persistence.jpa.repositories.SimulationRepository;
import com.mivivienda.platform.simuladordecredito.core.infrastructure.reports.SimulationReportGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SimulationExportQueryService {
    private final SimulationRepository simulationRepository;
    private final SimulationReportGenerator reportGenerator;

    public SimulationExportQueryService(SimulationRepository simulationRepository,
                                        SimulationReportGenerator reportGenerator) {
        this.simulationRepository = simulationRepository;
        this.reportGenerator = reportGenerator;
    }

    public Optional<byte[]> exportSimulation(Long id, String format) {
        Optional<Simulation> simulation = simulationRepository.findById(id);
        if (simulation.isEmpty()) return Optional.empty();
        return reportGenerator.generate(simulation.get(), format);
    }
}
