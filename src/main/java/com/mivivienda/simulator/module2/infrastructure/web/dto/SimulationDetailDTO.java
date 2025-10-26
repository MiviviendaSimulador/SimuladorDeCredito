package com.mivivienda.simulator.module2.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record SimulationDetailDTO(
        UUID id,
        String title,
        OffsetDateTime createdAt,
        BigDecimal tcea,
        BigDecimal van,
        BigDecimal tir,
        BigDecimal monthlyPayment,
        String inputsJson,
        String outputsJson,
        List<PaymentDTO> schedule
) {}
