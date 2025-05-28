package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.resident;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident.CreateResidentResource;

public class CreateResidentCommandFromResourceAssembler {
    public static CreateResidentCommand toCommandFromResource(CreateResidentResource resource) {
        return new CreateResidentCommand(
                resource.firstName(),
                resource.lastName());
    }
}