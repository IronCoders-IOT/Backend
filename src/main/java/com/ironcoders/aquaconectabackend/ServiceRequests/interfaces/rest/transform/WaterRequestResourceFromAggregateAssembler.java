package com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.aggregates.WaterSupplyRequest;
import com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.resources.WaterSupplyRequestResource;

public class WaterRequestResourceFromAggregateAssembler {
    public static WaterSupplyRequestResource toResourceFromEntity(WaterSupplyRequest aggregate) {
        return new WaterSupplyRequestResource(
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
