package com.ironcoders.aquaconectabackend.subcriptions.application.internal.comandservices;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.Device;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.UpdateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.subscription.SubscriptionCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription.SubscriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService
{

    private final SubscriptionRepository subscriptionRepository;
    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> handle(CreateSubscriptionCommand command) {

        Subscription subscription = new Subscription(command);

        subscriptionRepository.save(subscription);

        return Optional.of(subscription);
    }



    @Override
    public Optional<Subscription> handle(UpdateSubscriptionCommand command) {
        Optional<Subscription> optional = subscriptionRepository.findById(command.id());

        if (optional.isEmpty()) {
            return Optional.empty();
        }

        Subscription subscription = optional.get();

        // Actualiza solo si vienen nuevos valores (opcional)
        if (command.status() != null) {
            subscription.setStatus(command.status());
        }
        if (command.endDate() != null) {
            subscription.setEndDate(command.endDate());
        }

        subscriptionRepository.save(subscription);
        return Optional.of(subscription);
    }


}
