package com.ironcoders.aquaconectabackend.management.application.internal.queryservices;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllWaterRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterRequestsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestQueryService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.WaterRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WaterRequestQueryServiceImpl implements WaterRequestQueryService {

    private final WaterRequestRepository waterRequestRepository;

    public WaterRequestQueryServiceImpl(WaterRequestRepository waterRequestRepository) {
        this.waterRequestRepository = waterRequestRepository;
    }

    @Override
    public Optional<WaterRequestAggregate> handle(GetWaterRequestByIdQuery query) {
        return waterRequestRepository.findById(query.waterRequestId());
    }

    @Override
    public List<WaterRequestAggregate> handle(GetAllWaterRequestsQuery query) {
        return waterRequestRepository.findAll();
    }

    @Override
    public List<WaterRequestAggregate> handle(GetWaterRequestsByResidentIdQuery query) {
        return waterRequestRepository.findByResidentId(query.residentId()); }
}