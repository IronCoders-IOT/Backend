package com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories;


import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.GetProviderByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface ProviderQueryService {
    Optional<Provider> handle(GetProviderByUserIdQuery query);

}
