package com.mivivienda.platform.simuladordecredito.core.interfaces.rest.transform;

import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.Simulation;
import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.SimulationLine;
import com.mivivienda.platform.simuladordecredito.core.interfaces.rest.resources.SimulationLineResource;
import com.mivivienda.platform.simuladordecredito.core.interfaces.rest.resources.SimulationResource;

import java.util.List;

public class SimulationResourceFromEntityAssembler {
    
    public static SimulationResource toResourceFromEntity(Simulation entity) {
        return new SimulationResource(
            entity.getId(),
            entity.getUserId(),
            entity.getPrecioVivienda(),
            entity.getCuotaInicialPct(),
            entity.getMoneda().name(),
            entity.getTasaTipo().name(),
            entity.getTasaValor(),
            entity.getCapitalizacionM(),
            entity.getPlazoMeses(),
            entity.getGraciaTipo().name(),
            entity.getGraciaMeses(),
            entity.getSeguroDesgravamenPct(),
            entity.getSeguroRiesgoPct(),
            entity.getOtrosCargosFijos(),
            entity.getBank() != null ? entity.getBank().getId() : null,
            
            // Resultados
            entity.getBbp(),
            entity.getMontoFinanciar(),
            entity.getTem(),
            entity.getCuotaBase(),
            entity.getCuotaTotalProm(),
            entity.getTcea(),
            entity.getTir(),
            entity.getVan(),
            
            // Líneas del cronograma
            entity.getLines().stream()
                .map(SimulationResourceFromEntityAssembler::toLineResource)
                .toList()
        );
    }
    
    private static SimulationLineResource toLineResource(SimulationLine line) {
        return new SimulationLineResource(
            line.getNumeroMes(),
            line.getSaldoInicial(),
            line.getCuotaBase(),
            line.getInteres(),
            line.getCapital(),
            line.getSeguroDesgravamen(),
            line.getSeguroRiesgo(),
            line.getOtrosCargos(),
            line.getCuotaTotal(),
            line.getSaldoFinal()
        );
    }
}