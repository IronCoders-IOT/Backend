package com.ironcoders.aquaconectabackend.subcriptions.interfaces.acl;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.subscription.SubscriptionCommandService;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class SubscriptionContextFacade {

    private final SubscriptionCommandService subscriptionCommandService;

    public SubscriptionContextFacade(SubscriptionCommandService subscriptionCommandService) {
        this.subscriptionCommandService = subscriptionCommandService;
    }

    /**
     * Creates a new subscription for a resident.
     *
     * @param command the information to create the subscription
     * @return the created Subscription
     */
    public Optional<Subscription> createSubscription(CreateSubscriptionCommand command) {
        return subscriptionCommandService.handle(command);
    }

}
