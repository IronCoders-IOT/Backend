package com.ironcoders.aquaconectabackend.subcriptions.interfaces.acl;

import com.ironcoders.aquaconectabackend.subcriptions.domain.services.subscription.SubscriptionCommandService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionContextFacade {

    private final SubscriptionCommandService subscriptionCommandService;

    public SubscriptionContextFacade(SubscriptionCommandService subscriptionCommandService) {
        this.subscriptionCommandService = subscriptionCommandService;
    }

    public void createDefaultSubscriptionForResident(Long residentId) {
        subscriptionCommandService.createForResident(residentId);
    }
}
