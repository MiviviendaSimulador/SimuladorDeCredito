package com.mivivienda.simulator.module2.infrastructure.persistence.repo;

import com.mivivienda.simulator.module2.infrastructure.persistence.entity.PaymentEntity;
import com.mivivienda.simulator.module2.infrastructure.persistence.entity.PaymentEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, PaymentEntityId> {
    List<PaymentEntity> findByIdSimulationIdOrderByIdPeriodAsc(UUID simulationId);
}
