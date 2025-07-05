package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterSupplyRequest;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllWaterSupplyRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterSupplyRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterSupplyRequestsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.profiles.domain.model.queries.GetWaterRequestsByResidentIdQuery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface WaterSupplyRequestQueryService {
    Optional<WaterSupplyRequest> handle(GetWaterSupplyRequestByIdQuery query);
    List<WaterSupplyRequest> handle(GetAllWaterSupplyRequestsQuery query);
    List<WaterSupplyRequest> handle(GetWaterRequestsByResidentIdQuery getWaterRequestsByResidentIdQuery);
}