package com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider;

import java.util.Objects;
public record CreateProviderCommand(String taxName, String ruc) {
    public CreateProviderCommand {
        Objects.requireNonNull(taxName);
        Objects.requireNonNull(ruc);
    }
}

