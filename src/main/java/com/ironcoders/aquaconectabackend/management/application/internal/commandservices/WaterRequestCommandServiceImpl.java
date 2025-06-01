package com.ironcoders.aquaconectabackend.management.application.internal.commandservices;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestCommandService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.WaterRequestRepository;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderRepository;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service

public class WaterRequestCommandServiceImpl implements WaterRequestCommandService {

    private final WaterRequestRepository waterRequestRepository;
    private final ResidentRepository residentRepository;
    private final ProviderRepository providerRepository;
    public WaterRequestCommandServiceImpl(WaterRequestRepository waterRequestRepository, ResidentRepository residentRepository, ProviderRepository providerRepository) {
        this.waterRequestRepository = waterRequestRepository;
        this.residentRepository = residentRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public Optional<WaterRequestAggregate> handle(CreateWaterRequestCommand command) throws AccessDeniedException {
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

        // Validación y asignación de deliveredAt

        WaterRequestAggregate waterRequest = new WaterRequestAggregate(
                resident.getId(),
                resident.getProviderId(),
                command.requestedLiters(),
                command.status(),
                null
        );

        waterRequestRepository.save(waterRequest);
        return Optional.of(waterRequest);
    }



    @Override
    public Optional<WaterRequestAggregate> handle(UpdateWaterRequestCommand command) throws AccessDeniedException {
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


        // Buscar la solicitud de agua
        WaterRequestAggregate waterRequest = waterRequestRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada."));

        // Validar que el proveedor tenga permiso sobre esta solicitud
        if (!waterRequest.getProviderId().equals(provider.get().getId())) {
            throw new AccessDeniedException("No tienes permiso para actualizar esta solicitud.");
        }

        // Actualizar estado y fecha de entrega
        waterRequest.update(command.status(), command.deliveredAt());

        // Guardar cambios
        waterRequestRepository.save(waterRequest);

        return Optional.of(waterRequest);
    }



}