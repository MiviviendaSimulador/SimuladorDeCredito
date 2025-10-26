package com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates;

import com.mivivienda.platform.simuladordecredito.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bank extends AuditableAbstractAggregateRoot<Bank> {
    

    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String codigo;
    
    private String descripcion;
}

