package com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.commands.CreateWaterSupplyRequestCommand;
import com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.resources.CreateWaterRequestResource;

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