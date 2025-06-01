package com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider;

import java.util.Objects;
public record CreateProviderCommand(
        String taxName,
        String ruc,
        String firstName,
        String lastName,
        String email,
        String direction,
        String documentNumber,
        String documentType,
        String phone
) {
    public CreateProviderCommand {
        Objects.requireNonNull(taxName);
        Objects.requireNonNull(ruc);
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(email);
        Objects.requireNonNull(direction);
        Objects.requireNonNull(documentNumber);
        Objects.requireNonNull(documentType);
        Objects.requireNonNull(phone);
    }
}


