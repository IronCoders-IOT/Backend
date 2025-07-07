package com.ironcoders.aquaconectabackend.monitoring.interfaces.rest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ironcoders.aquaconectabackend.monitoring.domain.model.commads.CreateEventCommand;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.queries.GetAllEventsBySensorId;
import com.ironcoders.aquaconectabackend.monitoring.domain.services.EventCommandService;
import com.ironcoders.aquaconectabackend.monitoring.domain.services.EventQueryService;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources.CreateEventResource;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources.EventResource;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.transform.CreateEventCommandFromResourceAssembler;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.transform.EventResourceFromEntityAssembler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
@Tag(name="Events", description = "Event Management endpoints")
@PreAuthorize("isAuthenticated()")
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


}


