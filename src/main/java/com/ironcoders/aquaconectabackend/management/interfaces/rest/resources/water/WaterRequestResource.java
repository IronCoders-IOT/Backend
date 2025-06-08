package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.water;


import java.time.LocalDateTime;

public record WaterRequestResource(
        Long id,
        Long residentId,
        Long providerId,
        String requestedLiters,
        String emissionDate,
        String status,
        LocalDateTime deliveredAt
) {}