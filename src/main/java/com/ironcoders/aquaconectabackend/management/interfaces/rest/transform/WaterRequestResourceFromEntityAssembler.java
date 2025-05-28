package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.WaterRequestResource;

public class WaterRequestResourceFromAggregateAssembler {
    public static WaterRequestResource toResource(WaterRequestAggregate aggregate) {
        return new WaterRequestResource(
                aggregate.getId(),
                aggregate.getResidentId(),
                aggregate.getProviderId(),
                aggregate.getRequestedLiters(),
                aggregate.getStatus(),
                aggregate.getDeliveredAt()
        );
    }
}
