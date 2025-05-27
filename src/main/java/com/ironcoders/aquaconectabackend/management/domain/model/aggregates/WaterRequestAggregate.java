package com.ironcoders.aquaconectabackend.management.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "water_requests")
public class WaterRequestAggregate extends AuditableAbstractAggregateRoot<WaterRequestAggregate> {

    @NotNull
    @Column(name = "resident_id")
    private Long residentId;

    @NotNull
    @Column(name = "provider_id")
    private Long providerId;

    @NotNull
    @Column(name = "requested_liters")
    private String requestedLiters;

    @NotBlank
    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    public WaterRequestAggregate() {}

    public WaterRequestAggregate(Long residentId, Long providerId, String requestedLiters, String status, LocalDateTime deliveredAt) {
        this.residentId = residentId;
        this.providerId = providerId;
        this.requestedLiters = requestedLiters;
        this.status = status;
        this.deliveredAt = deliveredAt;
    }

    public WaterRequestAggregate update(String requestedLiters, String status, LocalDateTime deliveredAt) {
        this.requestedLiters = requestedLiters;
        this.status = status;
        this.deliveredAt = deliveredAt;
        return this;
    }
}