package com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands;

import java.util.Objects;

public record CreateSubscriptionCommand(Long sensorId, Long residentId) {

    public CreateSubscriptionCommand{
        Objects.requireNonNull(sensorId);
        Objects.requireNonNull(residentId);
    }
}
