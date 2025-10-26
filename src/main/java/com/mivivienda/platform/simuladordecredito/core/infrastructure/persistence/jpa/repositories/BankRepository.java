package com.mivivienda.platform.simuladordecredito.core.infrastructure.persistence.jpa.repositories;

import com.mivivienda.platform.simuladordecredito.core.domain.model.aggregates.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
}