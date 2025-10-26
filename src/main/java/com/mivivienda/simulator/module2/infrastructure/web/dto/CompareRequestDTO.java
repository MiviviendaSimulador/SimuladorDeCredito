package com.mivivienda.simulator.module2.infrastructure.web.dto;

import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record CompareRequestDTO(
        @Size(min = 2, message = "Se requieren al menos 2 IDs para comparar")
        List<UUID> ids
) {}
