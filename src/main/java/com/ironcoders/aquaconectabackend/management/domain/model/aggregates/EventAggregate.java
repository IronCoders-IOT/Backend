package com.ironcoders.aquaconectabackend.management.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "events")
public class EventAggregate extends AuditableAbstractAggregateRoot<EventAggregate> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvent;

    @NotBlank
    @Column(name = "event_type", length = 255)
    private String eventType;

    @NotBlank
    @Column(name = "quality_value", length = 255)
    private String qualityValue;

    @NotBlank
    @Column(name = "level_value", length = 255)
    private String levelValue;

    @NotNull
    @Column(name = "sensor_id")
    private Integer sensorId;

    public EventAggregate() {}

    public EventAggregate(String eventType, String qualityValue, String levelValue, Integer sensorId) {
        this.eventType = eventType;
        this.qualityValue = qualityValue;
        this.levelValue = levelValue;
        this.sensorId = sensorId;
    }

    public EventAggregate update(String eventType, String qualityValue, String levelValue, Integer sensorId) {
        this.eventType = eventType;
        this.qualityValue = qualityValue;
        this.levelValue = levelValue;
        this.sensorId = sensorId;
        return this;
    }
}