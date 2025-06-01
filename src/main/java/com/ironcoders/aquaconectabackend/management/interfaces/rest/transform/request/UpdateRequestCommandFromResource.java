package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.request;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request.UpdateRequestResource;

public class UpdateRequestCommandFromResource {

    public static UpdateRequestCommand toCommandFromResource(Long id, UpdateRequestResource resource){
        return new UpdateRequestCommand(id, resource.status());
    }

}
