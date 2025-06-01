package com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptions;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptionsByResidentId;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetSubscriptionByUserId;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryService {
    List<Subscription>handle(GetAllSubscriptionsByResidentId query) throws AccessDeniedException;
    Optional<Subscription> handle(GetSubscriptionByUserId query);
    List<Subscription>handle(GetAllSubscriptions query) throws AccessDeniedException;

}
