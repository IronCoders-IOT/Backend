package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.RequestResource;

public class RequestResourceFromAggregateAssembler {
    public static RequestResource toResource(RequestAggregate aggregate) {
        return new RequestResource(
                aggregate.getId(),
                aggregate.getResidentId(),
                aggregate.getProviderId(),
                aggregate.getTitle(),
                aggregate.getDescription(),
                aggregate.getStatus()
        );
    }
}