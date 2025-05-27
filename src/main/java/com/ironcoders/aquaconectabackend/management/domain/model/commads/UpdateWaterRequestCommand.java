package com.ironcoders.aquaconectabackend.management.domain.model.commads;

import java.time.LocalDateTime;

public record UpdateWaterRequestCommand(
        Long waterRequestId,
        String requestedLiters,
        String status,
        LocalDateTime deliveredAt
) {}