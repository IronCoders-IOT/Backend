package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform;


import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.resources.ProfileResource;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.ProviderResource;

public class ProviderResourceFromEntityAssembler {
    public static ProviderResource toResourceFromEntity(Provider entity) {
        return new ProviderResource(entity.getId(),entity.getTaxName(), entity.getRuc(),entity.getUserId());

    }


}
