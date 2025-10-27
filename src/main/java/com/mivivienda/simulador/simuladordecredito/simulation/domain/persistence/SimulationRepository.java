package com.mivivienda.simulador.simuladordecredito.simulation.domain.persistence;

import com.mivivienda.simulador.simuladordecredito.simulation.domain.model.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, UUID> {
    List<Simulation> findByUserId(UUID userId);
}

