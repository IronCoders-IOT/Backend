package com.ironcoders.aquaconectabackend.management.interfaces.rest;

import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllEventsBySensorId;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllDevicesByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetDeviceByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetDeviceByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.services.EventQueryService;
import com.ironcoders.aquaconectabackend.management.domain.services.DeviceQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.DeviceResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.EventResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.EventResourceFromEntityAssembler;

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
public class DeviceController {

    private final DeviceQueryService sensorQueryService;
    private final EventQueryService eventQueryService;

    public DeviceController(DeviceQueryService sensorQueryService, EventQueryService eventQueryService) {
        this.sensorQueryService = sensorQueryService;
        this.eventQueryService = eventQueryService;
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public ResponseEntity<DeviceResource> getSensorByResidentId(@PathVariable Long sensorId) {
        return sensorQueryService.handle(new GetDeviceByIdQuery(sensorId))
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
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
