package com.ironcoders.aquaconectabackend.profiles.domain.model.DTO;

import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Resident;

public record ResidentWithCredentials(
        Resident resident,
        String username,
        String password
) {
}
