package com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident;

import java.util.Objects;

public record CreateResidentCommand(
        String firstName,
        String lastName,
        String email,
        String direction,
        String documentNumber,
        String documentType,
        String phone
) {
    public CreateResidentCommand {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(email);
        Objects.requireNonNull(direction);
        Objects.requireNonNull(documentNumber);
        Objects.requireNonNull(documentType);
        Objects.requireNonNull(phone);
    }
}
