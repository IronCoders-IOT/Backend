package com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptionsByResidentId;

import java.util.List;

public interface SubscriptionQueryService {
    List<Subscription>handle(GetAllSubscriptionsByResidentId query);
}
