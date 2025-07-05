package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.Event;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateEventCommand;

import java.util.Optional;

public interface EventCommandService {
    Optional<Event> handle(CreateEventCommand command);

    Optional<Event> handle(UpdateEventCommand command);

}