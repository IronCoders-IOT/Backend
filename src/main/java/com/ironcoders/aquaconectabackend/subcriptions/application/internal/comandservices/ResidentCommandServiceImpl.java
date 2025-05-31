package com.ironcoders.aquaconectabackend.subcriptions.application.internal.comandservices;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.iam.interfaces.acl.IamContextFacade;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.ironcoders.aquaconectabackend.profiles.interfaces.acl.ProfilesContextFacade.ProfilesContextFacade;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.ResidentWithCredentials;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Subscription;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.resident.UpdateResidentCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.subscription.CreateSubscriptionCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.resident.ResidentCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentRepository;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.acl.SubscriptionContextFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResidentCommandServiceImpl implements ResidentCommandService {

    private final ResidentRepository residentRepository;
    private final ProfileRepository profileRepository;
    private final SubscriptionContextFacade subscriptionContextFacade;
    private final IamContextFacade iamContextFacade;
    private final ProfilesContextFacade profilesContextFacade;

    public ResidentCommandServiceImpl(ResidentRepository residentRepository, ProfileRepository profileRepository, SubscriptionContextFacade subscriptionContextFacade, IamContextFacade iamContextFacade, ProfilesContextFacade profilesContextFacade) {
        this.residentRepository = residentRepository;
        this.profileRepository = profileRepository;
        this.subscriptionContextFacade = subscriptionContextFacade;
        this.iamContextFacade = iamContextFacade;

        this.profilesContextFacade = profilesContextFacade;
    }

    @Override
    public ResidentWithCredentials handle(CreateResidentCommand command) {
        // 1. Obtener usuario autenticado (el proveedor que está registrando)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long providerId = userDetails.getId();

        // 2. Validar que el proveedor tenga perfil
        if (!profileRepository.findById(providerId).isPresent()) {
            throw new IllegalArgumentException("No se encontró un perfil para este proveedor");
        }

        // 3. Crear nuevo usuario IAM con ROLE_RESIDENT
        String username = command.documentNumber();
        String password = command.documentNumber();

        Long newUserId = iamContextFacade.createUser(
                username,
                password,
                List.of("ROLE_RESIDENT")
        );
        if (newUserId == 0L) {
            throw new IllegalArgumentException("No se pudo crear el usuario residente");
        }

        // 4. Crear su perfil en otro bounded context
        profilesContextFacade.createProfileForResident(
                newUserId,
                command.firstName(),
                command.lastName(),
                command.email(),
                command.direction(),
                command.documentNumber(),
                command.documentType(),
                command.phone()
        );

        // 5. Crear y guardar el residente
        Resident resident = new Resident(command, newUserId, providerId);
        residentRepository.save(resident);

        // 6. Crear suscripción automáticamente
        subscriptionContextFacade.createDefaultSubscriptionForResident(resident.getId());

        return new ResidentWithCredentials(resident, username, password);
    }

    @Override
    public Optional<Resident> handle(UpdateResidentCommand command) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<Resident> existingResident = residentRepository.findByUserId(userDetails.getId());
        if (existingResident.isEmpty()) {
            throw new IllegalArgumentException("No resident found for this user");
        }
        Resident resident = existingResident.get(0);
        resident.update(command);
        residentRepository.save(resident);

        return Optional.of(resident);

    }
}
