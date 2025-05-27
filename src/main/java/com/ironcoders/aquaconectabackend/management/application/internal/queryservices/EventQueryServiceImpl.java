package com.ironcoders.aquaconectabackend.management.application.internal.queryservices;


import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.EventAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllEventsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetEventByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetEventsBySensorIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.EventQueryService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventQueryServiceImpl implements EventQueryService {

    private final EventRepository eventRepository;

    public EventQueryServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Optional<EventAggregate> handle(GetEventByIdQuery query) {
        return eventRepository.findById(query.eventId());
    }

    @Override
    public List<EventAggregate> handle(GetAllEventsQuery query) {
        return eventRepository.findAll();
    }

    @Override
    public List<EventAggregate> handle(GetEventsBySensorIdQuery query) {
        return eventRepository.findBySensorId(query.sensorId());
    }
}