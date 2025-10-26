package com.mivivienda.simulator.module2.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentDTO(
        int period,
        LocalDate dueDate,
        BigDecimal installment,
        BigDecimal interest,
        BigDecimal principal,
        BigDecimal balance,
        BigDecimal insurances
) {}
