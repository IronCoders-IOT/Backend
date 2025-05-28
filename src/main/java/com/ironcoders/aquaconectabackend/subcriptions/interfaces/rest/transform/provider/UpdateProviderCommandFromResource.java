package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.provider;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.UpdateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.provider.UpdateProviderResource;

public class UpdateProviderCommandFromResource {
    public static UpdateProviderCommand toCommandFromResource(UpdateProviderResource resource) {
        return new UpdateProviderCommand(resource.taxName(), resource.ruc());


    }
}
