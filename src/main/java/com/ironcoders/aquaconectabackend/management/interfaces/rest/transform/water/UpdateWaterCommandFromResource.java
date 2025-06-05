package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.water;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.water.UpdateWaterResource;

public class UpdateWaterCommandFromResource {

    public static UpdateWaterRequestCommand toCommandFromResource(Long id, UpdateWaterResource resource) {
        return new UpdateWaterRequestCommand(id,resource.status(),resource.deliveredAt());
    }
}
