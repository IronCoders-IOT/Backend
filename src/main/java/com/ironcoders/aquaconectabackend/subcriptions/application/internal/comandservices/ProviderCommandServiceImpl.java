package com.ironcoders.aquaconectabackend.subcriptions.application.internal.comandservices;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.CreateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.commands.UpdateProviderCommand;
import com.ironcoders.aquaconectabackend.subcriptions.domain.services.ProviderCommandService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.ProviderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderCommandServiceImpl implements ProviderCommandService {

    private final ProviderRepository providerRepository;

    public ProviderCommandServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Override
    public Optional<Provider> handle(CreateProviderCommand command) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        var provider = new Provider(command, userDetails.getId());
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
