package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;


import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateEventCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.CreateEventResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.EventResource;

public class EventCommandFromResourceAssembler {
    public static CreateEventCommand toCreateCommand(CreateEventResource resource) {
        return new CreateEventCommand(
                resource.eventType(),
                resource.qualityValue(),
                resource.levelValue(),
                resource.sensorId()
        );
    }

    public static UpdateEventCommand toUpdateCommand(Long id, EventResource resource) {
        return new UpdateEventCommand(
                id,
                resource.eventType(),
                resource.qualityValue(),
                resource.levelValue(),
                resource.sensorId()
        );
    }
}