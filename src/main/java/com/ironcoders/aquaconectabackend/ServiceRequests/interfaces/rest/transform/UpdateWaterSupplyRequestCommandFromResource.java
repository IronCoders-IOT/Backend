package com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.transform;

import com.ironcoders.aquaconectabackend.ServiceRequests.domain.model.commands.UpdateWaterSupplyRequestCommand;
import com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.resources.UpdateWaterResource;

public class UpdateWaterSupplyRequestCommandFromResource {

    public static UpdateWaterSupplyRequestCommand toCommandFromResource(Long id, UpdateWaterResource resource, Long userId) {
        return new UpdateWaterSupplyRequestCommand(id,resource.status(),resource.deliveredAt(), userId);
    }
}
