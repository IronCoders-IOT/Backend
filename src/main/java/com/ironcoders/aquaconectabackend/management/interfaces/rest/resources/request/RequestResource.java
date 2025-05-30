package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request;

public record RequestResource(
        Long id,
        String title,
        String description,
        Long residentId,
        Long providerId
) {}