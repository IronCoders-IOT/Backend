package com.ironcoders.aquaconectabackend.management.application.internal.commandservices;


import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.EventAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.services.EventCommandService;
import com.ironcoders.aquaconectabackend.management.infrastructure.persistence.jpa.repositories.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EventCommandServiceImpl implements EventCommandService {

    private final EventRepository eventRepository;

    public EventCommandServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void handle(CreateEventCommand command) {
        EventAggregate event = new EventAggregate(
                command.eventType(),
                command.qualityValue(),
                command.levelValue(),
                command.sensorId()
        );
        eventRepository.save(event);
    }

    @Override
    public void handle(UpdateEventCommand command) {
        EventAggregate event = eventRepository.findById(command.eventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.update(
                command.eventType(),
                command.qualityValue(),
                command.levelValue(),
                command.sensorId()
        );
        eventRepository.save(event);
    }

    @Override
    public void handle(DeleteEventCommand command) {
        eventRepository.deleteById(command.eventId());
    }
}
