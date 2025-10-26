package com.mivivienda.platform.simuladordecredito.core.interfaces.rest.resources;

import java.math.BigDecimal;

public record SimulationLineResource(
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
) {}