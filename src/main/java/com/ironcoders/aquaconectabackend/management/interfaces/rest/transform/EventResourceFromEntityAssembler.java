package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;


import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.Event;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.CreateEventResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.EventResource;

public class EventResourceFromEntityAssembler {
    public static EventResource toResourceFromEntity(Event entity) {
        return new EventResource(
                entity.getId(),
                entity.getEventType(),
                entity.getQualityValue(),
                entity.getLevelValue(),
                entity.getSensorId()

        );
    }
}