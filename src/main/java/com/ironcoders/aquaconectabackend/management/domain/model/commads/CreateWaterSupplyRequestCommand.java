package com.ironcoders.aquaconectabackend.management.domain.model.commads;

import java.time.LocalDateTime;

public record CreateWaterSupplyRequestCommand(
        Long residentId,
        Long providerId,
        String requestedLiters,
        String status,
        LocalDateTime deliveredAt,
        Long userId     
) {}