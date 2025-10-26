package com.mivivienda.platform.simuladordecredito.core.infrastructure.persistence.jpa.repositories;

import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Long> {
    List<Simulation> findByUserId(Long userId);
}
