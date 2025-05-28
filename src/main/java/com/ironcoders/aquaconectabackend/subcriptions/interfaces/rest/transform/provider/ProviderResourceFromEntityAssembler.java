package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.provider;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.provider.ProviderResource;

public class ProviderResourceFromEntityAssembler {
    public static ProviderResource toResourceFromEntity(Provider entity) {
        return new ProviderResource(entity.getId(),entity.getTaxName(), entity.getRuc(),entity.getUserId());

    }


}
