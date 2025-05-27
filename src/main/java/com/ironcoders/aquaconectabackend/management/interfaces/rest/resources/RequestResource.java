package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources;

public record RequestResource(
        Long id,
        Long residentId,
        Long providerId,
        String title,
        String description,
        String status
) {}