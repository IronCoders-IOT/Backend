package com.ironcoders.aquaconectabackend.management.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
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
    private String emissionDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private LocalDateTime deliveredAt;

    public WaterRequestAggregate(Long residentId, Long providerId, String requestedLiters, String status, LocalDateTime deliveredAt) {
        this.residentId = residentId;
        this.providerId = providerId;
        this.requestedLiters = requestedLiters;
        this.status = status;
        this.deliveredAt = null;
        this.emissionDate= LocalDateTime.now().toString();

    }

    public WaterRequestAggregate(CreateWaterRequestCommand command) {
        this.residentId = command.residentId();
        this.providerId = command.providerId();
        this.requestedLiters = command.requestedLiters();
        this.status = command.status();
        this.deliveredAt = command.deliveredAt();
        this.emissionDate= LocalDateTime.now().toString();
    }

    public WaterRequestAggregate update(String status, LocalDateTime deliveredAt) {
        this.status = status;
        this.deliveredAt = deliveredAt;
        return this;
    }
    public WaterRequestAggregate() {
    }


}
