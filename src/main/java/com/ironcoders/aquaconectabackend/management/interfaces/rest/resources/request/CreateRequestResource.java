package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request;

public record CreateRequestResource(
        String title,
        String description,
        String status,
        Long residentId,
        Long providerId
) {}