package com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates;

public record ResidentWithCredentials(
        Resident resident,
        String username,
        String password
) {
}
