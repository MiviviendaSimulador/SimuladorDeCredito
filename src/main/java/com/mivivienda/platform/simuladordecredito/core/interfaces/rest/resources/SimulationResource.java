package com.mivivienda.platform.simuladordecredito.core.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;

public record SimulationResource(
    Long id,
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
    Long bankId,
    
    // Resultados
    BigDecimal bbp,
    BigDecimal montoFinanciar,
    BigDecimal tem,
    BigDecimal cuotaBase,
    BigDecimal cuotaTotalProm,
    BigDecimal tcea,
    BigDecimal tir,
    BigDecimal van,
    
    List<SimulationLineResource> lines
) {}