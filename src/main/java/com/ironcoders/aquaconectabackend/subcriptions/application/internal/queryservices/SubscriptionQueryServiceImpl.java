package com.ironcoders.aquaconectabackend.subcriptions.application.internal.queryservices;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptions;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetAllSubscriptionsByResidentId;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.subscription.GetSubscriptionByUserId;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderRepository;
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
    private final ProviderRepository providerRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository, ResidentRepository residentRepository, ProviderRepository providerRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.residentRepository = residentRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public List<Subscription> handle(GetAllSubscriptionsByResidentId query) throws AccessDeniedException {
        // 1. Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // 2. Obtener el proveedor asociado al usuario
        Provider provider = providerRepository.findByUserId(userId).get(0);
        if (provider == null) {
            throw new AccessDeniedException("No existe el proveedor");
        }



        // 2. Validar que sea un PROVIDER
        boolean isProvider = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PROVIDER"));

        if (!isProvider) {
            throw new AccessDeniedException("Solo los usuarios con rol PROVIDER pueden acceder a esta información.");
        }

        // 3. Validar que el residente pertenezca a ese provider
        Optional<Resident> residentOpt = residentRepository.findById(query.residentId());
        if (residentOpt.isEmpty()) {
            throw new AccessDeniedException("El residente no existe.");
        }

        Resident resident = residentOpt.get();
        System.out.println("🔐 Authenticated Provider ID: " + provider.getId());
        System.out.println("👤 Resident's Provider ID: " + residentOpt.get().getProviderId());

        if (!resident.getProviderId().equals(provider.getId())) {
            throw new AccessDeniedException("No tienes permiso para acceder a las suscripciones de este residente.");
        }

        // 4. Obtener suscripciones del residente

        List<Subscription> subscriptions = subscriptionRepository.findByResidentId(resident.getId());

        System.out.println("🔎 Subscripciones encontradas: " + subscriptions.size());
        subscriptions.forEach(s -> System.out.println("📦 Subscription ID: " + s.getId()));
        if (subscriptions.isEmpty()) {
            throw new AccessDeniedException("Este residente no tiene suscripciones.");
        }

        return subscriptions;
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
        Long userId = userDetails.getId();

        // 2. Obtener el proveedor asociado al usuario
        Provider provider = providerRepository.findByUserId(userId).get(0);
        if (provider == null) {
            throw new AccessDeniedException("No existe el proveedor");
        }


        System.out.println("PROVIDER ID AUTENTICADO: " + provider);

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
                if (residentProviderId.equals(provider.getId())) {
                    filtered.add(subscription);
                }
            } else {
                System.out.println("No se encontró el residente con ID: " + subscription.getResidentId());
            }
        }

        return filtered;
    }



}
