package com.ironcoders.aquaconectabackend.management.domain.model.commads;

import java.time.LocalDateTime;

public record CreateWaterRequestCommand(
        Long residentId,
        Long providerId,
        String requestedLiters,
        String status,
        LocalDateTime deliveredAt
) {}