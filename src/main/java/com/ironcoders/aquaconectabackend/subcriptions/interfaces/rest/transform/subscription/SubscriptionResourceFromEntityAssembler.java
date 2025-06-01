package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.subscription;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription.SubscriptionResource;

public class SubscriptionResourceFromEntityAssembler {

    public static SubscriptionResource toResourceFromEntity(Subscription entity) {
        return new SubscriptionResource(entity.getId(), entity.getStartDate().toString(),
                entity.getEndDate().toString(), entity.getStatus(), entity.getSensorId(),
                entity.getResidentId());
    }
}
