package com.ironcoders.aquaconectabackend.subcriptions.domain.services.subscription;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.UpdateSubscriptionCommand;

import java.util.Optional;

public interface SubscriptionCommandService {
    Optional<Subscription> handle (CreateSubscriptionCommand command);

    void createForResident(Long residentId);

    Optional<Subscription>handle(UpdateSubscriptionCommand command);
}
