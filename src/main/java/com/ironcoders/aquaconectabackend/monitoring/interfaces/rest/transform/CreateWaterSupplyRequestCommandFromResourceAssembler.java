package com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.commads.CreateWaterSupplyRequestCommand;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources.CreateWaterRequestResource;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources.WaterSupplyRequestResource;

public class CreateWaterSupplyRequestCommandFromResourceAssembler {
    public static CreateWaterSupplyRequestCommand toCommandFromResource(CreateWaterRequestResource resource, Long userId) {
        return new CreateWaterSupplyRequestCommand(
                resource.residentId(),
                resource.providerId(),
                resource.requestedLiters(),
                resource.status(),
                resource.deliveredAt(),
                userId
        );
    }

}