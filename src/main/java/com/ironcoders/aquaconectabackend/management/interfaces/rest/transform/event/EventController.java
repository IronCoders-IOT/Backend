package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.event;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllEventsBySensorId;
import com.ironcoders.aquaconectabackend.management.domain.services.EventCommandService;
import com.ironcoders.aquaconectabackend.management.domain.services.EventQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.event.CreateEventResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.event.EventResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
@Tag(name="Events", description = "Event Management endpoints")
public class EventController {

    private final EventCommandService eventCommandService;
    private final EventQueryService eventQueryService;

    public EventController(EventCommandService eventCommandService, EventQueryService eventQueryService) {
        this.eventCommandService = eventCommandService;
        this.eventQueryService = eventQueryService;
    }

    @PostMapping
    public ResponseEntity<EventResource> createEvent(@RequestBody CreateEventResource resource){

        CreateEventCommand createEventCommand = CreateEventCommandFromResourceAssembler.toCommandFromResource(resource);
        var event = eventCommandService.handle(createEventCommand);
        if (event.isEmpty())   return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var eventResource= EventResourceFromEntityAssembler.toResourceFromEntity(event.get());
        return new ResponseEntity<>(eventResource, HttpStatus.CREATED);

    }

    @GetMapping("/sensor/{id}")
    public ResponseEntity<List<EventResource>> getEventsBySensorId(@PathVariable Long id) {
        var events = eventQueryService.handle(new GetAllEventsBySensorId(id));
        var resources = events.stream()
                .map(EventResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }
}


