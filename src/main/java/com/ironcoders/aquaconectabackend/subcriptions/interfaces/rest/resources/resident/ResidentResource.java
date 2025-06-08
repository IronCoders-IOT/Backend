package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident;

public record ResidentResource(
        Long id,
        String firstName,
        String lastName,
        String phone,
        String address,
        Long providerId,
        Long userId,
        String username,
        String password
) {}
