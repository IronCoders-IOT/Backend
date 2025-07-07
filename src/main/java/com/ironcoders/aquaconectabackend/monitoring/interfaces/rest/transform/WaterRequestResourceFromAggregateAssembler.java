package com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.aggregates.WaterSupplyRequest;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources.WaterSupplyRequestResource;

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
