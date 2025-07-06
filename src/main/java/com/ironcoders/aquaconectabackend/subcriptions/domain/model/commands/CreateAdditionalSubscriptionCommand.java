package com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands;

import java.util.Objects;

public record CreateAdditionalSubscriptionCommand(Long residentId) {

        public CreateAdditionalSubscriptionCommand{
        Objects.requireNonNull(residentId);
    }
}