package com.ironcoders.aquaconectabackend.subcriptions.application.internal.queryservices;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptions;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptionsByResidentId;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetSubscriptionByUserId;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentRepository;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription.SubscriptionQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.subscription.SubscriptionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;
    private final ResidentRepository residentRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository, ResidentRepository residentRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.residentRepository = residentRepository;
    }

    @Override
    public List<Subscription> handle(GetAllSubscriptionsByResidentId query) throws AccessDeniedException {
        // 1. Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long providerId = userDetails.getId();

        // 2. Validar que sea un PROVIDER
        boolean isProvider = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PROVIDER"));

        if (!isProvider) {
            throw new AccessDeniedException("Solo los usuarios con rol PROVIDER pueden acceder a esta información.");
        }

        // 3. Validar que el residente pertenezca a ese provider
        Optional<Resident> residentOpt = residentRepository.findById(query.residentId());
        if (residentOpt.isEmpty() || !residentOpt.get().getProviderId().equals(providerId)) {
            throw new AccessDeniedException("No tienes permiso para acceder a las suscripciones de este residente.");
        }

        // 4. Obtener suscripciones
        return subscriptionRepository.findByResidentId(query.residentId());
    }


    @Override
    public Optional<Subscription> handle(GetSubscriptionByUserId query) {
        return Optional.empty(); // puedes implementar la lógica aquí después
    }
    @Override
    public List<Subscription> handle(GetAllSubscriptions query) throws AccessDeniedException {
        // 1. Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long providerId = userDetails.getId();
        System.out.println("PROVIDER ID AUTENTICADO: " + providerId);

        // 2. Validar que tenga rol PROVIDER
        boolean isProvider = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PROVIDER"));
        if (!isProvider) {
            throw new AccessDeniedException("Solo los usuarios con rol PROVIDER pueden acceder a esta información.");
        }

        // 3. Filtrar suscripciones cuyos residentes tengan ese providerId
        List<Subscription> all = subscriptionRepository.findAll();
        List<Subscription> filtered = new ArrayList<>();

        for (Subscription subscription : all) {
            Optional<Resident> residentOpt = residentRepository.findById(subscription.getResidentId());
            if (residentOpt.isPresent()) {
                Long residentProviderId = residentOpt.get().getProviderId();
                System.out.println("Provider del residente con ID " + residentOpt.get().getId() + ": " + residentProviderId);
                if (residentProviderId.equals(providerId)) {
                    filtered.add(subscription);
                }
            } else {
                System.out.println("No se encontró el residente con ID: " + subscription.getResidentId());
            }
        }

        return filtered;
    }



}
