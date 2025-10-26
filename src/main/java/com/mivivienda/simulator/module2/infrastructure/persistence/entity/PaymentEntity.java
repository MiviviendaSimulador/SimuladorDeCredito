package com.mivivienda.simulator.module2.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment_schedule")
public class PaymentEntity {

    @EmbeddedId
    private PaymentEntityId id;

    @Column(name = "due_date")
    private LocalDate dueDate;

    private BigDecimal installment;
    private BigDecimal interest;
    private BigDecimal principal;
    private BigDecimal balance;
    private BigDecimal insurances;

    public PaymentEntityId getId() { return id; }
    public void setId(PaymentEntityId id) { this.id = id; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public BigDecimal getInstallment() { return installment; }
    public void setInstallment(BigDecimal installment) { this.installment = installment; }
    public BigDecimal getInterest() { return interest; }
    public void setInterest(BigDecimal interest) { this.interest = interest; }
    public BigDecimal getPrincipal() { return principal; }
    public void setPrincipal(BigDecimal principal) { this.principal = principal; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public BigDecimal getInsurances() { return insurances; }
    public void setInsurances(BigDecimal insurances) { this.insurances = insurances; }
}
