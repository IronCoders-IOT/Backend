package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.subscription;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription.CreateSubscriptionResource;

public class CreateSubscriptionCommandFromResourceAssembler {

    public static CreateSubscriptionCommand toCommandFromResource( CreateSubscriptionResource resource) {
        return new CreateSubscriptionCommand(
            resource.sensorId(),
                resource.residentId()
        );
    }

}
