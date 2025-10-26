package com.mivivienda.simulator.module2.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class PaymentEntityId implements Serializable {
    private UUID simulationId;
    private Integer period;

    public PaymentEntityId() {}
    public PaymentEntityId(UUID simulationId, Integer period) {
        this.simulationId = simulationId;
        this.period = period;
    }

    public UUID getSimulationId() { return simulationId; }
    public void setSimulationId(UUID simulationId) { this.simulationId = simulationId; }
    public Integer getPeriod() { return period; }
    public void setPeriod(Integer period) { this.period = period; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentEntityId that)) return false;
        return Objects.equals(simulationId, that.simulationId) &&
               Objects.equals(period, that.period);
    }
    @Override public int hashCode() { return Objects.hash(simulationId, period); }
}
