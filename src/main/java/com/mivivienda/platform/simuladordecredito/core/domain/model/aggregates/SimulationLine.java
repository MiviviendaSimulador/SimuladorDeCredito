package com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates;

import com.mivivienda.platform.simuladordecredito.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class SimulationLine extends AuditableAbstractAggregateRoot<SimulationLine> {
    @Column(nullable = false)
    private Integer numeroMes;  // 1, 2, 3... hasta el plazo

    // Saldo al inicio del mes
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoInicial;

    // Cuota sin seguros
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal cuotaBase;

    // Interés del mes
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal interes;

    // Amortización de capital (lo que reduce la deuda)
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal capital;

    // Seguros
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal seguroDesgravamen;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal seguroRiesgo;

    // Otros cargos
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal otrosCargos;

    // Cuota total a pagar (todo sumado)
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal cuotaTotal;

    // Saldo al final del mes
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoFinal;

    // Relación con Simulation (Muchas líneas pertenecen a una simulación)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulation_id", nullable = false)
    private Simulation simulation;

    public SimulationLine() {

    }

    public SimulationLine(
            Simulation simulation,
            Integer numeroMes,
            BigDecimal saldoInicial,
            BigDecimal cuotaBase,
            BigDecimal interes,
            BigDecimal capital,
            BigDecimal seguroDesgravamen,
            BigDecimal seguroRiesgo,
            BigDecimal otrosCargos,
            BigDecimal cuotaTotal,
            BigDecimal saldoFinal
    ) {
        this.simulation = simulation;
        this.numeroMes = numeroMes;
        this.saldoInicial = saldoInicial;
        this.cuotaBase = cuotaBase;
        this.interes = interes;
        this.capital = capital;
        this.seguroDesgravamen = seguroDesgravamen;
        this.seguroRiesgo = seguroRiesgo;
        this.otrosCargos = otrosCargos;
        this.cuotaTotal = cuotaTotal;
        this.saldoFinal = saldoFinal;
    }
}
