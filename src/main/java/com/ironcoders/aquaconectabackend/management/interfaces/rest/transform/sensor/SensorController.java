package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.sensor;

import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllEventsBySensorId;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllSensorsByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetSensorByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetSensorByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.services.EventQueryService;
import com.ironcoders.aquaconectabackend.management.domain.services.SensorQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.event.EventResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.sensor.SensorResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.event.EventResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/sensors", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "sensors", description = "Sensor Management endpoints")
@PreAuthorize("isAuthenticated()")
public class SensorController {

    private final SensorQueryService sensorQueryService;
    private final EventQueryService eventQueryService;

    public SensorController(SensorQueryService sensorQueryService, EventQueryService eventQueryService) {
        this.sensorQueryService = sensorQueryService;
        this.eventQueryService = eventQueryService;
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public ResponseEntity<SensorResource> getSensorByResidentId(@PathVariable Long sensorId) {
        return sensorQueryService.handle(new GetSensorByIdQuery(sensorId))
                .map(SensorResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/{id}/events")
    public ResponseEntity<List<EventResource>> getEventsBySensorId(@PathVariable Long id) {
        var events = eventQueryService.handle(new GetAllEventsBySensorId(id));
        var resources = events.stream()
                .map(EventResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

}
