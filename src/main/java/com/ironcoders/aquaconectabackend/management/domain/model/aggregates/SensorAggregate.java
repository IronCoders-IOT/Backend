package com.ironcoders.aquaconectabackend.management.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class SensorAggregate extends AuditableAbstractAggregateRoot<SensorAggregate> {

    @Column(nullable = false)
    private String type;
    @Column(nullable = false)

    private String status;
    @Column(nullable = false)

    private String description;
    @Column(nullable = false)
    private Long residentId;

    public SensorAggregate() {}
    public SensorAggregate(String type, String status, String description, Long residentId){
        this.type = type;
        this.status = status;
        this.description = description;
        this.residentId = residentId;

    }


}
