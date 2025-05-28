package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.resident;

public record ResidentResource(
        Long id, String firstName, String lastName, Long providerId, Long userId) { }
