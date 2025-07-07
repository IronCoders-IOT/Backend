package com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources;


import java.time.LocalDateTime;

public record WaterSupplyRequestResource(
        Long id,
        Long residentId,
        Long providerId,
        String requestedLiters,
        String emissionDate,
        String status,
        LocalDateTime deliveredAt
) {}