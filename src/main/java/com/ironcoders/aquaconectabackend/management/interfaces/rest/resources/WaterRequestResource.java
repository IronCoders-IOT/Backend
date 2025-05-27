package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources;


import java.time.LocalDateTime;

public record WaterRequestResource(
        Long id,
        Long residentId,
        Long providerId,
        String requestedLiters,
        String status,
        LocalDateTime deliveredAt
) {}