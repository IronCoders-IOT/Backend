package com.ironcoders.aquaconectabackend.ServiceRequests.interfaces.rest.resources;

import java.time.LocalDateTime;

public record CreateWaterRequestResource(
        Long residentId,
        Long providerId,
        String requestedLiters,
        String status,
        LocalDateTime deliveredAt) {
}
