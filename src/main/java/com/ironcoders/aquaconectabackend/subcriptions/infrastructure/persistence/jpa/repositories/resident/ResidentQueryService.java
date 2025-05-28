package com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.provider.GetProviderByUserIdQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResidentQueryService{
    Optional<Resident> handle(GetProviderByUserIdQuery query);
}
