package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.water;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.water.WaterRequestResource;

public class WaterRequestResourceFromAggregateAssembler {
    public static WaterRequestResource toResourceFromEntity(WaterRequestAggregate aggregate) {
        return new WaterRequestResource(
                aggregate.getId(),
                aggregate.getResidentId(),
                aggregate.getProviderId(),
                aggregate.getRequestedLiters(),
                aggregate.getEmissionDate(),
                aggregate.getStatus(),
                aggregate.getDeliveredAt()
        );
    }
}
