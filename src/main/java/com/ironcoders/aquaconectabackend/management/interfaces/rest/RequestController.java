package com.ironcoders.aquaconectabackend.management.interfaces.rest;

import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.DeleteRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetRequestsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.RequestCommandService;
import com.ironcoders.aquaconectabackend.management.domain.services.RequestQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.RequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.RequestCommandFromResourceAssembler;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.RequestResourceFromAggregateAssembler;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/requests")
public class RequestController {

    private final RequestCommandService requestCommandService;
    private final RequestQueryService requestQueryService;

    public RequestController(RequestCommandService requestCommandService, RequestQueryService requestQueryService) {
        this.requestCommandService = requestCommandService;
        this.requestQueryService = requestQueryService;
    }

    @GetMapping
    public List<RequestResource> getAllRequests() {
        return requestQueryService.handle(new GetAllRequestsQuery())
                .stream()
                .map(RequestResourceFromAggregateAssembler::toResource)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestResource> getRequestById(@PathVariable Long id) {
        return requestQueryService.handle(new GetRequestByIdQuery(id))
                .map(RequestResourceFromAggregateAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/resident/{residentId}")
    public List<RequestResource> getRequestsByResident(@PathVariable Long residentId) {
        return requestQueryService.handle(new GetRequestsByResidentIdQuery(residentId))
                .stream()
                .map(RequestResourceFromAggregateAssembler::toResource)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Void> createRequest(@RequestBody RequestResource resource) {
        CreateRequestCommand command = RequestCommandFromResourceAssembler.toCreateCommand(resource);
        requestCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRequest(@PathVariable Long id, @RequestBody RequestResource resource) {
        UpdateRequestCommand command = RequestCommandFromResourceAssembler.toUpdateCommand(id, resource);
        requestCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        requestCommandService.handle(new DeleteRequestCommand(id));
        return ResponseEntity.noContent().build();
    }
}