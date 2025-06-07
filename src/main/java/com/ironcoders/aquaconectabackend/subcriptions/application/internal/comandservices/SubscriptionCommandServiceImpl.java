package com.ironcoders.aquaconectabackend.subcriptions.application.internal.comandservices;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.SensorAggregate;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.SensorRepository;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.UpdateSubscriptionCommand;
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
    private final SensorRepository sensorRepository;
    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository, SensorRepository sensorRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public Optional<Subscription> handle(CreateSubscriptionCommand command) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Long residentId = command.residentId();

        // 1. Crear el sensor automáticamente
        SensorAggregate sensor = new SensorAggregate(
                "ULTRASONICO",
                "ACTIVO",
                "Sensor automático creado con la suscripción",
                residentId
        );
        sensorRepository.save(sensor); // se genera su ID (asumiendo que es autogenerado)

        // 2. Crear la suscripción y asociarle el sensor
        Subscription subscription = new Subscription(command);

        subscriptionRepository.save(subscription);

        return Optional.of(subscription);
    }



    @Override
    public void createForResident(Long residentId) {
        // Crear el sensor asociado por defecto
        SensorAggregate sensor = new SensorAggregate(
                "ULTRASONICO",
                "ACTIVO",
                "Sensor automático creado con la suscripción",
                residentId
        );
        sensorRepository.save(sensor); // se genera su ID (asumiendo que es @GeneratedValue)

        // Crear la suscripción con el ID del sensor generado
        Subscription subscription = new Subscription(residentId, sensor.getId());
        subscriptionRepository.save(subscription);
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
