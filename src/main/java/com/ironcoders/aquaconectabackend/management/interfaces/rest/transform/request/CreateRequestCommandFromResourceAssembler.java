package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.request;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request.CreateRequestResource;
public class CreateRequestCommandFromResourceAssembler {

    public static CreateRequestCommand toCommandFromResource(CreateRequestResource resource) {
        return new CreateRequestCommand(
                resource.title(),
                resource.description(),
                resource.status(),
                resource.residentId(),
                resource.providerId()
                
        );
    }

}