package com.mivivienda.simulator.module2.infrastructure.persistence.repo;

import com.mivivienda.simulator.module2.infrastructure.persistence.entity.SimulationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SimulationJpaRepository extends JpaRepository<SimulationEntity, UUID> {
    Page<SimulationEntity> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    Optional<SimulationEntity> findByIdAndUserId(UUID id, String userId);
    List<SimulationEntity> findByIdInAndUserId(List<UUID> ids, String userId);
}
