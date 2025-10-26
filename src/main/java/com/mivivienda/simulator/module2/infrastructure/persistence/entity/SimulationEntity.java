package com.mivivienda.simulator.module2.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "simulations")
public class SimulationEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String title;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    private BigDecimal tcea;
    private BigDecimal van;
    private BigDecimal tir;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    // Usamos String para jsonb (simple y portable)
    @Column(name = "inputs_json", columnDefinition = "jsonb")
    private String inputsJson;

    @Column(name = "outputs_json", columnDefinition = "jsonb")
    private String outputsJson;

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public BigDecimal getTcea() { return tcea; }
    public void setTcea(BigDecimal tcea) { this.tcea = tcea; }
    public BigDecimal getVan() { return van; }
    public void setVan(BigDecimal van) { this.van = van; }
    public BigDecimal getTir() { return tir; }
    public void setTir(BigDecimal tir) { this.tir = tir; }
    public BigDecimal getMonthlyPayment() { return monthlyPayment; }
    public void setMonthlyPayment(BigDecimal monthlyPayment) { this.monthlyPayment = monthlyPayment; }
    public String getInputsJson() { return inputsJson; }
    public void setInputsJson(String inputsJson) { this.inputsJson = inputsJson; }
    public String getOutputsJson() { return outputsJson; }
    public void setOutputsJson(String outputsJson) { this.outputsJson = outputsJson; }
}
