package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.EventAggregate;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.EventResource;

public class EventResourceFromAggregateAssembler {
    public static EventResource toResource(EventAggregate aggregate) {
        return new EventResource(
                aggregate.getId(),
                aggregate.getEventType(),
                aggregate.getQualityValue(),
                aggregate.getLevelValue(),
                aggregate.getSensorId()
        );
    }
}