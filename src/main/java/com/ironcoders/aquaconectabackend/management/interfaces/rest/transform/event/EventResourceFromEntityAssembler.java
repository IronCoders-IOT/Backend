package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.event;


import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.EventAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.event.CreateEventResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.event.EventResource;

public class EventResourceFromEntityAssembler {
    public static EventResource toResourceFromEntity(EventAggregate entity) {
        return new EventResource(
                entity.getId(),
                entity.getEventType(),
                entity.getQualityValue(),
                entity.getLevelValue(),
                entity.getSensorId()

        );
    }
}