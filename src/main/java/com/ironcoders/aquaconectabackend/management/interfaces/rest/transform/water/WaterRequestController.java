package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.water;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllWaterRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterRequestsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestCommandService;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.water.CreateWaterRequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.water.WaterRequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.request.RequestResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/water-request", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "water-requests", description = "Request Management endpoints")

public class WaterRequestController {

    private final WaterRequestCommandService waterRequestCommandService;
    private final WaterRequestQueryService waterRequestQueryService;

    public WaterRequestController(WaterRequestCommandService waterRequestCommandService, WaterRequestQueryService waterRequestQueryService) {
        this.waterRequestCommandService = waterRequestCommandService;
        this.waterRequestQueryService = waterRequestQueryService;
    }

    @GetMapping
    public List<WaterRequestResource> getAllWaterRequests() {
        return waterRequestQueryService.handle(new GetAllWaterRequestsQuery())
                .stream()
                .map(WaterRequestResourceFromAggregateAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterRequestResource> getWaterRequestById(@PathVariable Long id) {
        return waterRequestQueryService.handle(new GetWaterRequestByIdQuery(id))
                .map(WaterRequestResourceFromAggregateAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/resident/{residentId}")
    public List<WaterRequestResource> getWaterRequestsByResident(@PathVariable Long residentId) {
        return waterRequestQueryService.handle(new GetWaterRequestsByResidentIdQuery(residentId))
                .stream()
                .map(WaterRequestResourceFromAggregateAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<WaterRequestResource> createWaterRequest(@RequestBody CreateWaterRequestResource resource) {

        CreateWaterRequestCommand command = CreateWaterRequestCommandFromResourceAssembler.toCommandFromResource(resource);
        var waterRequestAggregate = waterRequestCommandService.handle(command);
        if (waterRequestAggregate.isEmpty())  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var waterResource = WaterRequestResourceFromAggregateAssembler.toResourceFromEntity(waterRequestAggregate.get());
        return new ResponseEntity<>(waterResource, HttpStatus.CREATED);

    }


}