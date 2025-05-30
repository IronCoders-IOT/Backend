package com.ironcoders.aquaconectabackend.subcriptions.application.internal.queryservices;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptions;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptionsByResidentId;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetSubscriptionByUserId;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription.SubscriptionQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Subscription> handle(GetAllSubscriptionsByResidentId query) {
        return subscriptionRepository.findByResidentId(query.residentId());
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByUserId query) {
        return Optional.empty(); // puedes implementar la lógica aquí después
    }

    @Override
    public List<Subscription> handle(GetAllSubscriptions query) {
        return subscriptionRepository.findAll();
    }
}
