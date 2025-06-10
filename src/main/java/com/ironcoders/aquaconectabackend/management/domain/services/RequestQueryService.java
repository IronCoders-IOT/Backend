package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestsByProviderIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestsByResidentIdQuery;

import java.util.List;
import java.util.Optional;


public interface RequestQueryService {
    Optional<RequestAggregate> handle(GetRequestByIdQuery query);
    List<RequestAggregate> handle(GetAllRequestsByResidentIdQuery query);
    List<RequestAggregate> handle(GetAllRequestsByProviderIdQuery query);
    List<RequestAggregate> handle(GetAllRequestQuery query);
}