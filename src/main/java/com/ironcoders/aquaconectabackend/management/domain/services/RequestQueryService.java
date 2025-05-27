package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetRequestsByResidentIdQuery;
import io.jsonwebtoken.security.Request;

import java.util.List;
import java.util.Optional;


public interface RequestQueryService {
    Optional<RequestAggregate> handle(GetRequestByIdQuery query);
    List<RequestAggregate> handle(GetAllRequestsQuery query);
    List<RequestAggregate> handle(GetRequestsByResidentIdQuery query);
}