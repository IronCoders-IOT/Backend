package com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.water;

import java.time.LocalDateTime;

public record CreateWaterRequestResource(
        Long residentId,
        Long providerId,
        String requestedLiters,
        String status,
        LocalDateTime deliveredAt) {
}
