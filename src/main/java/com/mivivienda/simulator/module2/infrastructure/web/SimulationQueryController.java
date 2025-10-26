package com.mivivienda.simulator.module2.infrastructure.web;

import com.mivivienda.simulator.common.error.BadRequestException;
import com.mivivienda.simulator.common.error.NotFoundException;
import com.mivivienda.simulator.module2.infrastructure.persistence.entity.PaymentEntity;
import com.mivivienda.simulator.module2.infrastructure.persistence.entity.PaymentEntityId;
import com.mivivienda.simulator.module2.infrastructure.persistence.entity.SimulationEntity;
import com.mivivienda.simulator.module2.infrastructure.persistence.repo.PaymentJpaRepository;
import com.mivivienda.simulator.module2.infrastructure.persistence.repo.SimulationJpaRepository;
import com.mivivienda.simulator.module2.infrastructure.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/simulations")
public class SimulationQueryController {

    private final SimulationJpaRepository simulations;
    private final PaymentJpaRepository payments;

    public SimulationQueryController(SimulationJpaRepository simulations, PaymentJpaRepository payments) {
        this.simulations = simulations;
        this.payments = payments;
    }

    private String currentUserId() {
        return Optional.ofNullable(org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication())
                .map(a -> a.getName())
                .orElseThrow(() -> new BadRequestException("Usuario no autenticado"));
    }

    @GetMapping
    public Page<SimulationSummaryDTO> list(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, Math.max(1, size));
        Page<SimulationEntity> p = simulations.findByUserIdOrderByCreatedAtDesc(currentUserId(), pageable);
        return p.map(this::toSummary);
    }

    @GetMapping("/{id}")
    public SimulationDetailDTO detail(@PathVariable UUID id) {
        SimulationEntity s = simulations.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new NotFoundException("Simulación no encontrada"));
        List<PaymentDTO> sched = payments.findByIdSimulationIdOrderByIdPeriodAsc(s.getId()).stream()
                .map(this::toPaymentDTO)
                .toList();
        return new SimulationDetailDTO(
                s.getId(), s.getTitle(), s.getCreatedAt(),
                s.getTcea(), s.getVan(), s.getTir(), s.getMonthlyPayment(),
                s.getInputsJson(), s.getOutputsJson(), sched
        );
    }

    @PostMapping("/compare")
    public CompareResultDTO compare(@Valid @RequestBody CompareRequestDTO req) {
        if (req.ids() == null || req.ids().size() < 2) {
            throw new BadRequestException("Se requieren al menos 2 IDs");
        }
        String userId = currentUserId();
        List<SimulationEntity> found = simulations.findByIdInAndUserId(req.ids(), userId);
        Map<UUID, SimulationEntity> map = found.stream()
                .collect(Collectors.toMap(SimulationEntity::getId, x -> x));
        List<SimulationEntity> ordered = req.ids().stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .toList();
        if (ordered.size() < 2) throw new NotFoundException("Alguna simulación no existe o no pertenece al usuario");

        SimulationEntity base = ordered.get(0);
        MathContext mc = new MathContext(8, RoundingMode.HALF_UP);

        List<CompareResultDTO.Entry> entries = ordered.stream().map(s -> {
            BigDecimal dTcea = deltaPct(base.getTcea(), s.getTcea(), mc);
            BigDecimal dTir  = deltaPct(base.getTir(),  s.getTir(),  mc);
            BigDecimal dVan  = deltaAbs(base.getVan(),  s.getVan(),  mc);
            BigDecimal dPay  = deltaAbs(base.getMonthlyPayment(), s.getMonthlyPayment(), mc);
            return new CompareResultDTO.Entry(
                    s.getId(), s.getTitle(),
                    s.getTcea(), s.getVan(), s.getTir(), s.getMonthlyPayment(),
                    dTcea, dVan, dTir, dPay
            );
        }).toList();

        return new CompareResultDTO(base.getId(), entries);
    }

    @PostMapping("/{id}/duplicate")
    public SimulationSummaryDTO duplicate(@PathVariable UUID id, @RequestParam(required = false) String title) {
        SimulationEntity s = simulations.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new NotFoundException("Simulación no encontrada"));

        SimulationEntity copy = new SimulationEntity();
        copy.setId(UUID.randomUUID());
        copy.setUserId(s.getUserId());
        copy.setTitle(Objects.requireNonNullElse(title, s.getTitle() + " (copia)"));
        copy.setCreatedAt(OffsetDateTime.now());
        copy.setTcea(s.getTcea());
        copy.setVan(s.getVan());
        copy.setTir(s.getTir());
        copy.setMonthlyPayment(s.getMonthlyPayment());
        copy.setInputsJson(s.getInputsJson());
        copy.setOutputsJson(s.getOutputsJson());
        simulations.save(copy);

        List<PaymentEntity> src = payments.findByIdSimulationIdOrderByIdPeriodAsc(s.getId());
        for (PaymentEntity p : src) {
            PaymentEntity dup = new PaymentEntity();
            dup.setId(new PaymentEntityId(copy.getId(), p.getId().getPeriod()));
            dup.setDueDate(p.getDueDate());
            dup.setInstallment(p.getInstallment());
            dup.setInterest(p.getInterest());
            dup.setPrincipal(p.getPrincipal());
            dup.setBalance(p.getBalance());
            dup.setInsurances(p.getInsurances());
            payments.save(dup);
        }
        return toSummary(copy);
    }

    // Helpers
    private SimulationSummaryDTO toSummary(SimulationEntity s) {
        return new SimulationSummaryDTO(
                s.getId(), s.getTitle(), s.getCreatedAt(),
                s.getTcea(), s.getVan(), s.getTir(), s.getMonthlyPayment()
        );
    }
    private PaymentDTO toPaymentDTO(PaymentEntity p) {
        return new PaymentDTO(
                p.getId().getPeriod(),
                p.getDueDate(),
                p.getInstallment(),
                p.getInterest(),
                p.getPrincipal(),
                p.getBalance(),
                p.getInsurances()
        );
    }
    private BigDecimal deltaPct(BigDecimal base, BigDecimal value, MathContext mc) {
        if (base == null || value == null || base.compareTo(BigDecimal.ZERO) == 0) return null;
        return value.subtract(base, mc).divide(base, mc).multiply(BigDecimal.valueOf(100), mc);
    }
    private BigDecimal deltaAbs(BigDecimal base, BigDecimal value, MathContext mc) {
        if (base == null || value == null) return null;
        return value.subtract(base, mc);
    }
}
