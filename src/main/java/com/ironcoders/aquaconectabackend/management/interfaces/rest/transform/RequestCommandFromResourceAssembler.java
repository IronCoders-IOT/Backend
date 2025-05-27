package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform;


import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateRequestCommand;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.RequestResource;


public class RequestCommandFromResourceAssembler {
    public static CreateRequestCommand toCreateCommand(RequestResource resource) {
        return new CreateRequestCommand(
                resource.residentId(),
                resource.providerId(),
                resource.title(),
                resource.description(),
                resource.status()
        );
    }

    public static UpdateRequestCommand toUpdateCommand(Long id, RequestResource resource) {
        return new UpdateRequestCommand(
                id,
                resource.title(),
                resource.description(),
                resource.status()
        );
    }
}