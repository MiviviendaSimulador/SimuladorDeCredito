package com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates;

import com.mivivienda.platform.simuladordecredito.core.domain.model.commands.CreateSimulationCommand;
import com.mivivienda.platform.simuladordecredito.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.mivivienda.platform.simuladordecredito.core.domain.model.valueobjects.CurrencyCode;
import com.mivivienda.platform.simuladordecredito.core.domain.model.valueobjects.GraceType;
import com.mivivienda.platform.simuladordecredito.core.domain.model.valueobjects.RateType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Simulation extends AuditableAbstractAggregateRoot<Simulation> {
    private Long userId;

    @Column(nullable = false)
    private BigDecimal precioVivienda;

    @Column(nullable = false)
    private BigDecimal cuotaInicialPct;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private CurrencyCode moneda;            // PEN | USD

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private RateType tasaTipo;              // EA | TNA

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal tasaValor;           // 0.095

    @Column(precision = 10, scale = 0)
    private Integer capitalizacionM;        // si TNA; null si EA

    @Column(nullable = false)
    private Integer plazoMeses;             // 60..300

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private GraceType graciaTipo;           // NONE | TOTAL | PARCIAL

    @Column(nullable = false)
    private Integer graciaMeses;            // 0..24

    // Seguros / Cargos (entrada)
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal seguroDesgravamenPct;  // ej 0.0009

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal seguroRiesgoPct;       // ej 0.0006

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal otrosCargosFijos;      // ej 15.00

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = true)
    private Bank bank;

    // --- Intermedios útiles / Resultados (resumen) ---
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal bbp;                 // Bono aplicado

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal montoFinanciar;     // PV - CI - BBP

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal tem;                 // tasa efectiva mensual

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal cuotaBase;           // sin seguros/cargos

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal cuotaTotalProm;      // promedio con costos

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal tcea;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal tir;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal van;                 // define tasa desc.

    // Relación 1..N con líneas
    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SimulationLine> lines = new ArrayList<>();

    public Simulation() {
        this.lines = new ArrayList<>();
    }

    public Simulation(CreateSimulationCommand command){
        this.userId = command.userId();
        this.precioVivienda = command.precioVivienda();
        this.cuotaInicialPct = command.cuotaInicialPct();
        this.moneda = command.moneda();
        this.tasaTipo = command.tasaTipo();
        this.tasaValor = command.tasaValor();
        this.capitalizacionM = command.capitalizacionM();
        this.plazoMeses = command.plazoMeses();
        this.graciaTipo = command.graciaTipo();
        this.graciaMeses = command.graciaMeses();
        this.seguroDesgravamenPct = command.seguroDesgravamenPct();
        this.seguroRiesgoPct = command.seguroRiesgoPct();
        this.otrosCargosFijos = command.otrosCargosFijos();

        // Bank se asignará después desde el bankId
        this.bank = null;

        // Resultados calculados - iniciarlos en 0 o null según corresponda
        this.bbp = BigDecimal.ZERO;
        this.montoFinanciar = BigDecimal.ZERO;
        this.tem = BigDecimal.ZERO;
        this.cuotaBase = BigDecimal.ZERO;
        this.cuotaTotalProm = BigDecimal.ZERO;
        this.tcea = BigDecimal.ZERO;
        this.tir = BigDecimal.ZERO;
        this.van = BigDecimal.ZERO;

        // Lista de líneas vacía al inicio
        this.lines = new ArrayList<>();
    }
}
