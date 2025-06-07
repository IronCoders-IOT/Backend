package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.sensor;

import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllSensorsByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetSensorByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetSensorByResidentId;
import com.ironcoders.aquaconectabackend.management.domain.services.SensorQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.sensor.SensorResource;
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

    public SensorController(SensorQueryService sensorQueryService) {
        this.sensorQueryService = sensorQueryService;
    }


    @GetMapping("/{sensorId}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public ResponseEntity<SensorResource> getSensorByResidentId(@PathVariable Long sensorId) {
        return sensorQueryService.handle(new GetSensorByIdQuery(sensorId))
                .map(SensorResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/resident/{residentId}/all")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public List<SensorResource> getAllSensorsByResidentId(@PathVariable Long residentId) {
        return sensorQueryService.handle(new GetAllSensorsByResidentId(residentId))
                .stream()
                .map(SensorResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }
}
