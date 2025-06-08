package com.ironcoders.aquaconectabackend.subcriptions.application.internal.comandservices;

import com.ironcoders.aquaconectabackend.iam.domain.model.entities.Role;
import com.ironcoders.aquaconectabackend.iam.domain.model.valueobjects.Roles;
import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.ironcoders.aquaconectabackend.iam.interfaces.acl.IamContextFacade;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.CreateProfileCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.valueobjects.PersonName;
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
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderRepository;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentRepository;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.acl.SubscriptionContextFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
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
    private final ProviderRepository providerRepository;
    private final RoleRepository roleRepository;

    public ResidentCommandServiceImpl(ResidentRepository residentRepository, ProfileRepository profileRepository, SubscriptionContextFacade subscriptionContextFacade, IamContextFacade iamContextFacade, ProfilesContextFacade profilesContextFacade, ProviderRepository providerRepository, RoleRepository roleRepository) {
        this.residentRepository = residentRepository;
        this.profileRepository = profileRepository;
        this.subscriptionContextFacade = subscriptionContextFacade;
        this.iamContextFacade = iamContextFacade;

        this.profilesContextFacade = profilesContextFacade;
        this.providerRepository = providerRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public ResidentWithCredentials handle(CreateResidentCommand command) throws AccessDeniedException {
        // 1. Obtener usuario autenticado (el proveedor que está registrando)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        if (userDetails.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_PROVIDER"))) {
            throw new AccessDeniedException("Solo los usuarios con rol PROVIDER pueden registrar residentes.");
        }

        // 2. Obtener el proveedor asociado al usuario
        Provider provider = providerRepository.findByUserId(userId).get(0);
        if (provider == null) {
            throw new AccessDeniedException("No existe el proveedor");
        }


        Long providerId = provider.getId();

        // 3. Validar que el proveedor tenga perfil (si es necesario)
        if (!profileRepository.findByUserId(providerId).isEmpty()) {
            throw new IllegalArgumentException("No se encontró un perfil para este proveedor");
        }

        // 4. Crear nuevo usuario IAM con ROLE_RESIDENT
        // 1. Crear las credenciales del residente
        String username = command.firstName() + "." + command.lastName();
        String password = command.documentNumber();


    // 2. Indicar el rol por su nombre (como string)
        List<String> roles = List.of("ROLE_RESIDENT");
    // 3. Crear usuario en IAM
            Long newUserId = iamContextFacade.createUser(
                    username,
                    password,
                    roles
            );


        if (newUserId == 0L) {
            throw new IllegalArgumentException("No se pudo crear el usuario residente");
        }

        // 5. Crear su perfil en otro bounded context
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

        // 6. Crear y guardar el residente
        Resident resident = new Resident(command, newUserId, providerId);
        residentRepository.save(resident);

        // 7. Crear suscripción automáticamente
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
