package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.resident;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.UpdateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident.UpdateResidentResource;

public class UpdateResidentCommandFromResource {
    public static UpdateResidentCommand toCommandFromResource(UpdateResidentResource resource) {
        return new UpdateResidentCommand(resource.firstName(), resource.lastName());


    }
}
