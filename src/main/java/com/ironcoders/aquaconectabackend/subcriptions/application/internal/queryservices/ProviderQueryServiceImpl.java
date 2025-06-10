package com.ironcoders.aquaconectabackend.subcriptions.application.internal.queryservices;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.provider.GetAllProvidersQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.provider.GetProviderByUserIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderQueryServiceImpl implements ProviderQueryService {
    private final ProviderRepository providerRepository;

    public ProviderQueryServiceImpl(ProviderRepository profileRepository) {
        this.providerRepository = profileRepository;
    }


    @Override
    public Optional<Provider> handle(GetProviderByUserIdQuery query) {
        List<Provider> providers = providerRepository.findByUserId(query.userId());
        if (providers.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(providers.get(0));
    }

    @Override
    public Optional<Provider> findByUserId(long userId) {
        List<Provider> providers = providerRepository.findByUserId(userId);
        if (providers.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(providers.get(0));
    }

    @Override
    public List<Provider> handle(GetAllProvidersQuery query) {
        List<Provider>providers = providerRepository.findAll();
        return providers;
    }

}
