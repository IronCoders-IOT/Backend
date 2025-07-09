package com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands;

import java.util.Objects;

public record CreateSubscriptionCommand(Long deviceId, Long residentId, Long providerId,  Float waterTankSize) {

    public CreateSubscriptionCommand{
        Objects.requireNonNull(deviceId);
        Objects.requireNonNull(residentId);
        Objects.requireNonNull(providerId);
        Objects.requireNonNull(waterTankSize);
    }
}
