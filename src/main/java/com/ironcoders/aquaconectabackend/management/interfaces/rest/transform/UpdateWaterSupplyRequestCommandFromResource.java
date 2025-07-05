package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterSupplyRequestCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.UpdateWaterResource;

public class UpdateWaterSupplyRequestCommandFromResource {

    public static UpdateWaterSupplyRequestCommand toCommandFromResource(Long id, UpdateWaterResource resource, Long userId) {
        return new UpdateWaterSupplyRequestCommand(id,resource.status(),resource.deliveredAt(), userId);
    }
}
