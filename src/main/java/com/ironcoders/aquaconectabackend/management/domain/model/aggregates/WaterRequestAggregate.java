package com.ironcoders.aquaconectabackend.management.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class WaterRequestAggregate extends AuditableAbstractAggregateRoot<WaterRequestAggregate> {

    @Column(nullable = false)
    private Long residentId;

    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false)
    private String requestedLiters;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime deliveredAt;

    public WaterRequestAggregate() {}

    public WaterRequestAggregate(CreateWaterRequestCommand command) {
        this.residentId = command.residentId();
        this.providerId = command.providerId();
        this.requestedLiters = command.requestedLiters();
        this.status = command.status();
        this.deliveredAt = command.deliveredAt();
    }

    public WaterRequestAggregate update(String requestedLiters, String status, LocalDateTime deliveredAt) {
        this.requestedLiters = requestedLiters;
        this.status = status;
        this.deliveredAt = deliveredAt;
        return this;
    }
}