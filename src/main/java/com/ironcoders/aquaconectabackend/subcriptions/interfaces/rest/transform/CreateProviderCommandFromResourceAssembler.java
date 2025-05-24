package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.CreateProviderResource;

public class CreateProviderCommandFromResourceAssembler {
    public static CreateProviderCommand toCommandFromResource(CreateProviderResource resource) {
        return new CreateProviderCommand(
                resource.taxName(),
                resource.ruc());
    }
}