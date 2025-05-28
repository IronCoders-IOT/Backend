package com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident;

import java.util.Objects;

public record CreateResidentCommand(String firstName, String lastName)  {

    public CreateResidentCommand {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
    }

}

