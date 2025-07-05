package com.ironcoders.aquaconectabackend.profiles.application.internal.comandservices;

import com.ironcoders.aquaconectabackend.iam.domain.model.entities.Role;
import com.ironcoders.aquaconectabackend.iam.domain.model.valueobjects.Roles;
import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.ironcoders.aquaconectabackend.iam.interfaces.acl.IamContextFacade;
import com.ironcoders.aquaconectabackend.profiles.domain.model.DTO.ResidentWithCredentials;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.CreateProfileCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.UpdateResidentCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.valueobjects.PersonName;
import com.ironcoders.aquaconectabackend.profiles.domain.services.ResidentCommandService;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProviderRepository;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ResidentRepository;
import com.ironcoders.aquaconectabackend.profiles.interfaces.acl.ProfilesContextFacade.ProfilesContextFacade;
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
    private final IamContextFacade iamContextFacade;
    private final ProfilesContextFacade profilesContextFacade;
    private final ProviderRepository providerRepository;

    public ResidentCommandServiceImpl(ResidentRepository residentRepository, ProfileRepository profileRepository, IamContextFacade iamContextFacade, ProfilesContextFacade profilesContextFacade, ProviderRepository providerRepository, RoleRepository roleRepository) {
        this.residentRepository = residentRepository;
        this.profileRepository = profileRepository;
        this.iamContextFacade = iamContextFacade;

        this.profilesContextFacade = profilesContextFacade;
        this.providerRepository = providerRepository;
    }

    @Override
    public ResidentWithCredentials handle(CreateResidentCommand command) throws AccessDeniedException {
        // 1. Get the provider using the providerId from the command
        Long providerId = command.providerId(); // Adjust if your command class uses a different name

        Provider provider = providerRepository.findByUserId(providerId).get(0);
        if (provider == null) {
            throw new AccessDeniedException("Provider does not exist.");
        }

        // 2. Validate that the provider has a profile (if necessary)
        if (profileRepository.findByUserId(providerId).isEmpty()) {
            throw new IllegalArgumentException("No profile found for this provider.");
        }

        // 3. Create resident's credentials
        String username = command.firstName() + "." + command.lastName();
        String password = command.documentNumber();

        // 4. Set the resident role
        List<String> roles = List.of("ROLE_RESIDENT");

        // 5. Create the user in IAM
        Long newUserId = iamContextFacade.createUser(
                username,
                password,
                roles
        );

        if (newUserId == 0L) {
            throw new IllegalArgumentException("Could not create resident user.");
        }

        // 6. Create profile for the resident in another bounded context
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

        // 7. Create and save the resident
        Resident resident = new Resident(command, newUserId, provider.getId());
        residentRepository.save(resident);

        return new ResidentWithCredentials(resident, username, password);
    }

        @Override
        public Optional<Resident> handle(UpdateResidentCommand command) {

            // Get the userId from the command (adjust as needed)
            Long userId = command.userId(); // Make sure your command has this method/field

            List<Resident> existingResident = residentRepository.findByUserId(userId);
            if (existingResident.isEmpty()) {
                throw new IllegalArgumentException("No resident found for this user");
            }
            Resident resident = existingResident.get(0);
            resident.update(command);
            residentRepository.save(resident);

            return Optional.of(resident);
        }
}
