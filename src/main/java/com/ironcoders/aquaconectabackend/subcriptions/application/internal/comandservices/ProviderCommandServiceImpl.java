package com.ironcoders.aquaconectabackend.subcriptions.application.internal.comandservices;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.domain.model.valueobjects.PersonName;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.provider.UpdateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.provider.ProviderCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderCommandServiceImpl implements ProviderCommandService {

    private final ProviderRepository providerRepository;
    private final ProfileRepository profileRepository;

    public ProviderCommandServiceImpl(ProviderRepository providerRepository, ProfileRepository profileRepository) {
        this.providerRepository = providerRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Provider> handle(CreateProviderCommand command) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // Crear perfil si no existe
        if (profileRepository.findById(userId).isEmpty()) {
            PersonName name = new PersonName(command.firstName(), command.lastName());
            Profile profile = new Profile(
                    name,
                    command.email(),
                    command.direction(),
                    command.documentNumber(),
                    command.documentType(),
                    userId,
                    command.phone()
            );
            profileRepository.save(profile);
        }

        // Crear proveedor
        Provider provider = new Provider(command, userId);
        providerRepository.save(provider);

        return Optional.of(provider);
    }

    @Override
    public Optional<Provider> handle(UpdateProviderCommand command) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<Provider> existingProviders = providerRepository.findByUserId(userDetails.getId());
        if (existingProviders.isEmpty()) {
            throw new IllegalArgumentException("No provider found for this user");
        }
        Provider provider = existingProviders.get(0);
        provider.update(command);
        providerRepository.save(provider);

        return Optional.of(provider);

    }
}
