package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.provider;


import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.provider.ProviderResource;
public class ProviderResourceFromEntityAssembler {

    public static ProviderResource toResourceFromEntities(Provider provider, Profile profile) {
        return new ProviderResource(
                provider.getId(),
                provider.getTaxName(),
                provider.getRuc(),
                provider.getUserId(),
                profile.getName().getFirstName(),
                profile.getName().getLastName(),
                profile.getEmail(),
                profile.getDirection(),
                profile.getDocumentNumber(),
                profile.getDocumentType(),
                profile.getPhone()
        );
    }
}

