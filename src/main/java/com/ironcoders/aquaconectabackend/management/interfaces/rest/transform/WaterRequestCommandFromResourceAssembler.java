package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.WaterRequestResource;

public class WaterRequestCommandFromResourceAssembler {
    public static CreateWaterRequestCommand toCreateCommand(WaterRequestResource resource) {
        return new CreateWaterRequestCommand(
                resource.residentId(),
                resource.providerId(),
                resource.requestedLiters(),
                resource.status(),
                resource.deliveredAt()
        );
    }

    public static UpdateWaterRequestCommand toUpdateCommand(Long id, WaterRequestResource resource) {
        return new UpdateWaterRequestCommand(
                id,
                resource.requestedLiters(),
                resource.status(),
                resource.deliveredAt()
        );
    }
}