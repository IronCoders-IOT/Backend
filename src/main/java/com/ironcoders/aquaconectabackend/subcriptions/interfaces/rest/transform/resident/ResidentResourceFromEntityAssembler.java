package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.resident;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.provider.ProviderResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident.ResidentResource;

public class ResidentResourceFromEntityAssembler {
    public static ResidentResource toResourceFromEntity(Resident entity) {
        return new ResidentResource(entity.getId(),entity.getFirstName(), entity.getLastName(),entity.getProviderId(),entity.getUserId());

    }


}
