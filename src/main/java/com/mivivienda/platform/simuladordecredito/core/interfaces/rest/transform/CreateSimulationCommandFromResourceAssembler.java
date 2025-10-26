package com.mivivienda.platform.simuladordecredito.core.interfaces.rest.transform;

import com.mivivienda.platform.simuladordecredito.core.domain.model.commands.CreateSimulationCommand;
import com.mivivienda.platform.simuladordecredito.core.domain.model.valueobjects.CurrencyCode;
import com.mivivienda.platform.simuladordecredito.core.domain.model.valueobjects.GraceType;
import com.mivivienda.platform.simuladordecredito.core.domain.model.valueobjects.RateType;
import com.mivivienda.platform.simuladordecredito.core.interfaces.rest.resources.CreateSimulationResource;

public class CreateSimulationCommandFromResourceAssembler {
    
    public static CreateSimulationCommand toCommandFromResource(CreateSimulationResource resource) {
        return new CreateSimulationCommand(
            resource.userId(),
            resource.precioVivienda(),
            resource.cuotaInicialPct(),
            CurrencyCode.valueOf(resource.moneda()),
            RateType.valueOf(resource.tasaTipo()),
            resource.tasaValor(),
            resource.capitalizacionM(),
            resource.plazoMeses(),
            GraceType.valueOf(resource.graciaTipo()),
            resource.graciaMeses(),
            resource.seguroDesgravamenPct(),
            resource.seguroRiesgoPct(),
            resource.otrosCargosFijos(),
            resource.bankId()
        );
    }
}