package com.mivivienda.platform.simuladordecredito.core.domain.model.commands;

import com.mivivienda.platform.simuladordecredito.core.domain.model.valueobjects.CurrencyCode;
import com.mivivienda.platform.simuladordecredito.core.domain.model.valueobjects.GraceType;
import com.mivivienda.platform.simuladordecredito.core.domain.model.valueobjects.RateType;

import java.math.BigDecimal;

public record CreateSimulationCommand(
    Long userId,
    BigDecimal precioVivienda,
    BigDecimal cuotaInicialPct,
    CurrencyCode moneda,
    RateType tasaTipo,
    BigDecimal tasaValor,
    Integer capitalizacionM,  // null si EA
    Integer plazoMeses,
    GraceType graciaTipo,
    Integer graciaMeses,
    BigDecimal seguroDesgravamenPct,
    BigDecimal seguroRiesgoPct,
    BigDecimal otrosCargosFijos,
    Long bankId  // Solo el ID, no el objeto completo
) {}