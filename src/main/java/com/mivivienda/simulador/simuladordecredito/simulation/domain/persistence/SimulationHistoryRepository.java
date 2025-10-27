package com.mivivienda.simulador.simuladordecredito.simulation.domain.persistence;

import com.mivivienda.simulador.simuladordecredito.simulation.domain.model.SimulationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SimulationHistoryRepository extends JpaRepository<SimulationHistory, UUID> {
    List<SimulationHistory> findBySimulationId(UUID simulationId);
}

