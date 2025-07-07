package com.ironcoders.aquaconectabackend.monitoring.application.internal.commandservices;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.aggregates.Event;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.commads.UpdateEventCommand;
import com.ironcoders.aquaconectabackend.monitoring.domain.services.EventCommandService;
import com.ironcoders.aquaconectabackend.monitoring.infrastructure.persistence.jpa.repositories.EventRepository;

import java.util.Optional;

@Service
public class EventCommandServiceImpl implements EventCommandService {

    private final EventRepository eventRepository;

    public EventCommandServiceImpl(EventRepository eventRepository) {

        this.eventRepository = eventRepository;
    }

    @Override
    public Optional<Event> handle(CreateEventCommand command){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        var eventAggregate = new Event(command);

        eventRepository.save(eventAggregate);
        return Optional.of(eventAggregate);


    }

    @Override
    public Optional<Event> handle(UpdateEventCommand command) {
        return Optional.empty();
    }





}
