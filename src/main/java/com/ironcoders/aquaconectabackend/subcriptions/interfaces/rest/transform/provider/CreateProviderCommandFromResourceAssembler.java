package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.provider;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.provider.CreateProviderResource;
public class CreateProviderCommandFromResourceAssembler {
    public static CreateProviderCommand toCommandFromResource(CreateProviderResource resource) {
        return new CreateProviderCommand(
                resource.taxName(),
                resource.ruc(),
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.direction(),
                resource.documentNumber(),
                resource.documentType(),
                resource.phone()
        );
    }
}
