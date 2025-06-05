package com.ironcoders.aquaconectabackend.management.domain.services;


import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.EventAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllEventsBySensorId;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetEventByIdQuery;

import java.util.List;
import java.util.Optional;

public interface EventQueryService {
    Optional<EventAggregate> handle(GetEventByIdQuery query);
    List<EventAggregate> handle(GetAllEventsBySensorId query);
}