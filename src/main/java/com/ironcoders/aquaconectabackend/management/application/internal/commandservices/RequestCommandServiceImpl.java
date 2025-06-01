package com.ironcoders.aquaconectabackend.management.application.internal.commandservices;
import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.services.RequestCommandService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.RequestRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderRepository;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RequestCommandServiceImpl implements RequestCommandService {

    private final RequestRepository requestRepository;
    private final ResidentRepository residentRepository;
    private final ProviderRepository providerRepository;

    public RequestCommandServiceImpl(RequestRepository requestRepository, ResidentRepository residentRepository, ProviderRepository providerRepository) {

        this.requestRepository = requestRepository;
        this.residentRepository = residentRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public Optional<RequestAggregate> handle(CreateRequestCommand command) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        boolean isResident = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_RESIDENT"));
        if (!isResident) {
            throw new AccessDeniedException("Solo los residentes pueden generar solicitudes de agua.");
        }

        List<Resident> residents = residentRepository.findByUserId(userId);
        if (residents.isEmpty()) {
            throw new IllegalArgumentException("Residente no encontrado.");
        }

        Resident resident = residents.get(0);


        RequestAggregate requestAggregate = new RequestAggregate(
                resident.getId(),
                resident.getProviderId(),
                command.title(),
                command.description(),
                command.status()
        );

        requestRepository.save(requestAggregate);
        return Optional.of(requestAggregate);
    }

    @Override
    public Optional<RequestAggregate> handle(UpdateRequestCommand command) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        boolean isProvider = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROVIDER"));
        if (!isProvider) {
            throw new AccessDeniedException("Solo los proveedores pueden actualizar solicitudes.");
        }

        Optional<Provider> provider = providerRepository.findById(userId);

        if (provider.isEmpty()) {
            throw new IllegalArgumentException("Proveedores no encontrado.");
        }



        RequestAggregate requestAggregate =  requestRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada."));

        // Validar que el proveedor tenga permiso sobre esta solicitud
        if (!requestAggregate.getProviderId().equals(provider.get().getId())) {
            throw new AccessDeniedException("No tienes permiso para actualizar esta solicitud.");
        }

        requestAggregate.update(command.status());

        requestRepository.save(requestAggregate);
        return Optional.of(requestAggregate);
    }


        
}