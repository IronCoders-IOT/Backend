package com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription;

import java.time.LocalDate;

public record UpdateSubscriptionCommand(
        Long id,
        LocalDate endDate,
        String status
) {}