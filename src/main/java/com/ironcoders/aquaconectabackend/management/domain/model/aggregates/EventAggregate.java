package com.ironcoders.aquaconectabackend.management.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
public class EventAggregate extends AuditableAbstractAggregateRoot<EventAggregate> {

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String qualityValue;

    @Column(nullable = false)
    private String levelValue;

    @Column(nullable = false)
    private Long sensorId;

    public EventAggregate() {}

    public EventAggregate(String eventType, String qualityValue, String levelValue, Long sensorId) {
        this.eventType = eventType;
        this.qualityValue = qualityValue;
        this.levelValue = levelValue;
        this.sensorId = sensorId;
    }

    public EventAggregate(CreateEventCommand command){
        this.eventType= command.eventType();
        this.qualityValue = command.qualityValue();
        this.levelValue = command.levelValue();
        this.sensorId = command.sensorId();
    }

//    public EventAggregate update(String eventType, String qualityValue, String levelValue, Long sensorId) {
//        this.eventType = eventType;
//        this.qualityValue = qualityValue;
//        this.levelValue = levelValue;
//        this.sensorId = sensorId;
//        return this;
//    }
}