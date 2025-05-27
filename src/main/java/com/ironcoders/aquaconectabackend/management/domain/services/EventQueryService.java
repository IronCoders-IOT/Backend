package com.ironcoders.aquaconectabackend.management.domain.services;


import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.EventAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllEventsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetEventByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetEventsBySensorIdQuery;

import java.util.List;
import java.util.Optional;

public interface EventQueryService {
    Optional<EventAggregate> handle(GetEventByIdQuery query);
    List<EventAggregate> handle(GetAllEventsQuery query);
    List<EventAggregate> handle(GetEventsBySensorIdQuery query);
}