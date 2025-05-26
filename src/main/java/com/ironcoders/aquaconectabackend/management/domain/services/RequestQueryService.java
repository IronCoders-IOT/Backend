package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetRequestsByResidentIdQuery;
import io.jsonwebtoken.security.Request;

import java.util.List;
import java.util.Optional;

public interface RequestQueryService {
    Optional<Request> handle(GetRequestByIdQuery query);
    List<Request> handle(GetAllRequestsQuery query);
    List<Request> handle(GetRequestsByResidentIdQuery query);
}