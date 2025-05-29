package com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.UpdateSubscriptionCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {


    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Long sensorId;
    @Column(nullable = false)
    private Long residentId;


    public Subscription() {}

    public Subscription(CreateSubscriptionCommand command) {
        this.startDate = LocalDate.now(); // Fecha actual
        this.endDate = this.startDate.plusMonths(1); // Por ejemplo, un mes de suscripción
        this.status = "ACTIVE"; // Estado inicial, ajusta según tu lógica
        this.sensorId = command.sensorId();
        this.residentId = command.residentId(); // Corrige esto, antes tenías: this.residentId = this.residentId
    }


    public Subscription(UpdateSubscriptionCommand command) {

        this.startDate = LocalDate.now();
        this.endDate = this.startDate.plusMonths(1);
        this.status = "ACTIVE";
    }










}
