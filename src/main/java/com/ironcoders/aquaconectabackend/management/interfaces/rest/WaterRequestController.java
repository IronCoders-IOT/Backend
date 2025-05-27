package com.ironcoders.aquaconectabackend.management.interfaces.rest;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteWaterRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllWaterRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterRequestsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestCommandService;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterRequestQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.WaterRequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.WaterRequestCommandFromResourceAssembler;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.WaterRequestResourceFromAggregateAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/water-requests")
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
                .map(WaterRequestResourceFromAggregateAssembler::toResource)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterRequestResource> getWaterRequestById(@PathVariable Long id) {
        return waterRequestQueryService.handle(new GetWaterRequestByIdQuery(id))
                .map(WaterRequestResourceFromAggregateAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/resident/{residentId}")
    public List<WaterRequestResource> getWaterRequestsByResident(@PathVariable Long residentId) {
        return waterRequestQueryService.handle(new GetWaterRequestsByResidentIdQuery(residentId))
                .stream()
                .map(WaterRequestResourceFromAggregateAssembler::toResource)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Void> createWaterRequest(@RequestBody WaterRequestResource resource) {
        var command = WaterRequestCommandFromResourceAssembler.toCreateCommand(resource);
        waterRequestCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWaterRequest(@PathVariable Long id, @RequestBody WaterRequestResource resource) {
        var command = WaterRequestCommandFromResourceAssembler.toUpdateCommand(id, resource);
        waterRequestCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaterRequest(@PathVariable Long id) {
        waterRequestCommandService.handle(new DeleteWaterRequestCommand(id));
        return ResponseEntity.noContent().build();
    }
}