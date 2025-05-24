package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.UpdateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.UpdateProviderResource;

public class UpdateProviderCommandFromResource {
    public static UpdateProviderCommand toCommandFromResource(UpdateProviderResource resource) {
        return new UpdateProviderCommand(resource.taxName(), resource.ruc());


    }
}
