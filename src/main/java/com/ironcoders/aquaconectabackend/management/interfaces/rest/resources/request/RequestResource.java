package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request;

public record RequestResource(
        Long id,
        String title,
        String description,
        String status,
        Long residentId,
        Long providerId
) {}