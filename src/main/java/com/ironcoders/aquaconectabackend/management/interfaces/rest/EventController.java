package com.ironcoders.aquaconectabackend.management.interfaces.rest;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllEventsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetEventByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetEventsBySensorIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.EventCommandService;
import com.ironcoders.aquaconectabackend.management.domain.services.EventQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.EventResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.EventCommandFromResourceAssembler;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.EventResourceFromAggregateAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventCommandService eventCommandService;
    private final EventQueryService eventQueryService;

    public EventController(EventCommandService eventCommandService, EventQueryService eventQueryService) {
        this.eventCommandService = eventCommandService;
        this.eventQueryService = eventQueryService;
    }

    @GetMapping
    public List<EventResource> getAllEvents() {
        return eventQueryService.handle(new GetAllEventsQuery())
                .stream()
                .map(EventResourceFromAggregateAssembler::toResource)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResource> getEventById(@PathVariable Long id) {
        return eventQueryService.handle(new GetEventByIdQuery(id))
                .map(EventResourceFromAggregateAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sensor/{sensorId}")
    public List<EventResource> getEventsBySensor(@PathVariable Long sensorId) {
        return eventQueryService.handle(new GetEventsBySensorIdQuery(sensorId))
                .stream()
                .map(EventResourceFromAggregateAssembler::toResource)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody EventResource resource) {
        var command = EventCommandFromResourceAssembler.toCreateCommand(resource);
        eventCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable Long id, @RequestBody EventResource resource) {
        var command = EventCommandFromResourceAssembler.toUpdateCommand(id, resource);
        eventCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventCommandService.handle(new DeleteEventCommand(id));
        return ResponseEntity.noContent().build();
    }
}
