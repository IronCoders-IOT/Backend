package com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.subscription;

import java.time.LocalDate;

public record UpdateSubscriptionResource(
        String status,
        LocalDate endDate
) {}
