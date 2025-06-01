package com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.provider;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.provider.GetProviderByUserIdQuery;

import java.util.Optional;

public interface ProviderQueryService {
    Optional<Provider> handle(GetProviderByUserIdQuery query);

    Optional<Provider> findByUserId(long userId);
}
