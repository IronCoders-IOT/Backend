package com.ironcoders.aquaconectabackend.management.domain.services;

import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.EventAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateEventCommand;

import java.util.Optional;

public interface EventCommandService {
    Optional<EventAggregate> handle(CreateEventCommand command);

    Optional<EventAggregate> handle(UpdateEventCommand command);

    Optional<EventAggregate> handle(DeleteEventCommand command);
}