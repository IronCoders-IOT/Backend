package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.event;


import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.event.CreateEventResource;

public class CreateEventCommandFromResourceAssembler {
    public static CreateEventCommand toCommandFromResource(CreateEventResource resource) {
        return new CreateEventCommand(
                resource.eventType(),
                resource.qualityValue(),
                resource.levelValue(),
                resource.sensorId()
        );
    }


}