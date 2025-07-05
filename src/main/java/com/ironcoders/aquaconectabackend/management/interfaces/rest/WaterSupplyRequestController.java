package com.ironcoders.aquaconectabackend.management.interfaces.rest;

import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateWaterSupplyRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateWaterSupplyRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllWaterSupplyRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetWaterSupplyRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterSupplyRequestCommandService;
import com.ironcoders.aquaconectabackend.management.domain.services.WaterSupplyRequestQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.CreateWaterRequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.UpdateWaterResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.WaterSupplyRequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.CreateWaterSupplyRequestCommandFromResourceAssembler;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.UpdateWaterSupplyRequestCommandFromResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.WaterRequestResourceFromAggregateAssembler;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/water-requests", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "water-requests", description = "Request Management endpoints")
@PreAuthorize("isAuthenticated()")
public class WaterSupplyRequestController {

    private final WaterSupplyRequestCommandService waterRequestCommandService;
    private final WaterSupplyRequestQueryService waterRequestQueryService;

    public WaterSupplyRequestController(WaterSupplyRequestCommandService waterRequestCommandService, WaterSupplyRequestQueryService waterRequestQueryService) {
        this.waterRequestCommandService = waterRequestCommandService;
        this.waterRequestQueryService = waterRequestQueryService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public List<WaterSupplyRequestResource> getAllWaterRequests() {
        return waterRequestQueryService.handle(new GetAllWaterSupplyRequestsQuery())
                .stream()
                .map(WaterRequestResourceFromAggregateAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }



    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public ResponseEntity<WaterSupplyRequestResource> getWaterRequestById(@PathVariable Long id) {
        return waterRequestQueryService.handle(new GetWaterSupplyRequestByIdQuery(id))
                .map(WaterRequestResourceFromAggregateAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<WaterSupplyRequestResource> createWaterRequest(@RequestBody CreateWaterRequestResource resource) throws AccessDeniedException {
        // Get the user ID from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();


        CreateWaterSupplyRequestCommand command = CreateWaterSupplyRequestCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var waterRequestAggregate = waterRequestCommandService.handle(command);
        if (waterRequestAggregate.isEmpty())  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var waterResource = WaterRequestResourceFromAggregateAssembler.toResourceFromEntity(waterRequestAggregate.get());
        return new ResponseEntity<>(waterResource, HttpStatus.CREATED);

    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ResponseEntity<WaterSupplyRequestResource> updateWaterRequest(
            @PathVariable Long id,
            @RequestBody UpdateWaterResource resource) throws AccessDeniedException {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                    Long userId = userDetails.getId();

        UpdateWaterSupplyRequestCommand command = UpdateWaterSupplyRequestCommandFromResource.toCommandFromResource(id, resource, userId);

        Optional<?> result = waterRequestCommandService.handle(command);
        return result
                .map(obj -> WaterRequestResourceFromAggregateAssembler.toResourceFromEntity((com.ironcoders.aquaconectabackend.management.domain.model.aggregates.WaterSupplyRequest) obj))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

     
    }


}