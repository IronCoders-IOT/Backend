package com.ironcoders.aquaconectabackend.subcriptions.application.internal.queryservices;

import com.ironcoders.aquaconectabackend.subcriptions.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.provider.GetProviderByUserIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.resident.GetResidentByUserIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.resident.GetResidentsByProviderIdQuery;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentQueryService;
import com.ironcoders.aquaconectabackend.subcriptions.infrastructure.persistence.jpa.repositories.resident.ResidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentQueryServiceImpl implements ResidentQueryService {

    private final ResidentRepository residentRepository;

    public ResidentQueryServiceImpl(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }


    @Override
    public Optional<Resident> handle(GetResidentByUserIdQuery query) {

       return  residentRepository.findById(query.userId());
    }

    @Override
    public List<Resident> handle(GetResidentsByProviderIdQuery query) {
        return residentRepository.findByProviderId(query.providerId());
    }

    @Override
    public Optional<Resident> findByUserId(long userId) {
        return residentRepository.findById(userId);
    }

}
