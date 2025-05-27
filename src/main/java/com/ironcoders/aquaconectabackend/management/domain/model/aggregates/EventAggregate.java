package com.ironcoders.aquaconectabackend.management.domain.model.aggregates;

import com.ironcoders.aquaconectabackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
@Table(name = "events")
public class EventAggregate extends AuditableAbstractAggregateRoot<EventAggregate> {

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
    private Long sensorId;

    public EventAggregate() {}

    public EventAggregate(String eventType, String qualityValue, String levelValue, Long sensorId) {
        this.eventType = eventType;
        this.qualityValue = qualityValue;
        this.levelValue = levelValue;
        this.sensorId = sensorId;
    }

    public EventAggregate update(String eventType, String qualityValue, String levelValue, Long sensorId) {
        this.eventType = eventType;
        this.qualityValue = qualityValue;
        this.levelValue = levelValue;
        this.sensorId = sensorId;
        return this;
    }
}