package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.CreateSubscriptionResource;

public class CreateSubscriptionCommandFromResourceAssembler {

    public static CreateSubscriptionCommand toCommandFromResource( CreateSubscriptionResource resource) {
        return new CreateSubscriptionCommand(
            resource.sensorId(),
                resource.residentId()
        );
    }

}
