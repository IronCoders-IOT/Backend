package com.ironcoders.aquaconectabackend.management.application.internal.commandservices;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.EventAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.services.EventCommandService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.EventRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventCommandServiceImpl implements EventCommandService {

    private final EventRepository eventRepository;

    public EventCommandServiceImpl(EventRepository eventRepository) {

        this.eventRepository = eventRepository;
    }

    @Override
    public Optional<EventAggregate> handle(CreateEventCommand command){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        var eventAggregate = new EventAggregate(command);

        eventRepository.save(eventAggregate);
        return Optional.of(eventAggregate);


    }

    @Override
    public Optional<EventAggregate> handle(UpdateEventCommand command) {
        return Optional.empty();
    }

    @Override
    public Optional<EventAggregate> handle(DeleteEventCommand command) {
        return Optional.empty();
    }



}
