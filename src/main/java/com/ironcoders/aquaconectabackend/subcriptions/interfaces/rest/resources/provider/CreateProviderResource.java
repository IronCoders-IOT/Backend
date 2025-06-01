
package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.provider;

public record CreateProviderResource(
        String taxName,
        String ruc,
        String firstName,
        String lastName,
        String email,
        String direction,
        String documentNumber,
        String documentType,
        String phone
) {}
