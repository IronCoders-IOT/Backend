package com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident;

import java.util.Objects;

public record UpdateResidentCommand(String firstName, String lastName) {
    public UpdateResidentCommand {
        Objects.requireNonNull(firstName, "firstName cannot be null");
        Objects.requireNonNull(lastName, "lastName cannot be null");
    }

}
