package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.water;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.water.CreateWaterRequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.water.WaterRequestResource;

public class CreateWaterRequestCommandFromResourceAssembler {
    public static CreateWaterRequestCommand toCommandFromResource(CreateWaterRequestResource resource) {
        return new CreateWaterRequestCommand(
                resource.residentId(),
                resource.providerId(),
                resource.requestedLiters(),
                resource.status(),
                resource.deliveredAt()
        );
    }

}