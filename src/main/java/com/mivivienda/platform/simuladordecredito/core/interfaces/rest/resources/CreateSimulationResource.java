package com.mivivienda.platform.simuladordecredito.core.interfaces.rest.resources;

import java.math.BigDecimal;

public record CreateSimulationResource(
    Long userId,
    BigDecimal precioVivienda,
    BigDecimal cuotaInicialPct,
    String moneda,
    String tasaTipo,
    BigDecimal tasaValor,
    Integer capitalizacionM,
    Integer plazoMeses,
    String graciaTipo,
    Integer graciaMeses,
    BigDecimal seguroDesgravamenPct,
    BigDecimal seguroRiesgoPct,
    BigDecimal otrosCargosFijos,
    Long bankId
) {}