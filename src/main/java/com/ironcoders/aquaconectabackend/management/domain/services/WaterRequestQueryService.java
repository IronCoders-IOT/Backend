package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterRequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllWaterRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterRequestsByResidentIdQuery;

import java.util.List;
import java.util.Optional;

public interface WaterRequestQueryService {
    Optional<WaterRequestAggregate> handle(GetWaterRequestByIdQuery query);
    List<WaterRequestAggregate> handle(GetAllWaterRequestsQuery query);
    List<WaterRequestAggregate> handle(GetWaterRequestsByResidentIdQuery query);
}