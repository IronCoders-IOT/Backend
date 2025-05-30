package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.request;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request.RequestResource;

public class RequestResourceFromEntityAssembler {
    public static RequestResource toResourceFromEntity(RequestAggregate aggregate) {
        return new RequestResource(
            aggregate.getId(),
            aggregate.getTitle(),
                aggregate.getStatus(),
                aggregate.getProviderId(),
                aggregate.getProviderId()
        );
    }
}