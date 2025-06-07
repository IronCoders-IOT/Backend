package com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.resident.GetResidentByUserIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.resident.GetResidentsByProviderIdQuery;

import java.util.List;
import java.util.Optional;

public interface ResidentQueryService{
    Optional<Resident> handle(GetResidentByUserIdQuery query);
    List<Resident> handle(GetResidentsByProviderIdQuery query);
    List<Resident> findByUserId(Long userId);
}
