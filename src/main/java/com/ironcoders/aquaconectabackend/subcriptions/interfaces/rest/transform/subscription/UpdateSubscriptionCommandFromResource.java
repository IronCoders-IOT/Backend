package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.subscription;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.UpdateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription.UpdateSubscriptionResource;

public class UpdateSubscriptionCommandFromResource {
    public static UpdateSubscriptionCommand toCommand(Long id, UpdateSubscriptionResource resource) {
        return new UpdateSubscriptionCommand(
                id,
                resource.endDate(),
                resource.status()
        );
    }

}
