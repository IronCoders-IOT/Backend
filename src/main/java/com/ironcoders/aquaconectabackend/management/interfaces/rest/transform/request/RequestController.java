package com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.request;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.RequestAggregate;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateRequestCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestsByProviderIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllRequestsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetRequestByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.RequestCommandService;
import com.ironcoders.aquaconectabackend.management.domain.services.RequestQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request.CreateRequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request.RequestResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.request.UpdateRequestResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/requests")
@Tag(name = "Requests", description = "Request Management endpoints")
@PreAuthorize("isAuthenticated()")
public class RequestController {

    private final RequestCommandService requestCommandService;
    private final RequestQueryService requestQueryService;

    public RequestController(RequestCommandService requestCommandService, RequestQueryService requestQueryService) {
        this.requestCommandService = requestCommandService;
        this.requestQueryService = requestQueryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<RequestResource> createRequest(@RequestBody CreateRequestResource resource) throws AccessDeniedException {
        CreateRequestCommand createRequestCommand = CreateRequestCommandFromResourceAssembler.toCommandFromResource(resource);
        var request = requestCommandService.handle(createRequestCommand);
        if (request.isEmpty())  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var requestResource = RequestResourceFromEntityAssembler.toResourceFromEntity(request.get());
        return new ResponseEntity<>(requestResource, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ResponseEntity<RequestResource> updateRequest(
            @PathVariable Long id,
            @RequestBody UpdateRequestResource resource) throws AccessDeniedException {

        UpdateRequestCommand command = UpdateRequestCommandFromResource.toCommandFromResource(id, resource);

        Optional<RequestAggregate> result = requestCommandService.handle(command);

        return result
                .map(RequestResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public ResponseEntity<RequestResource> getRequestById(@PathVariable Long id) {
        return requestQueryService.handle(new GetRequestByIdQuery(id))
                .map(RequestResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/resident/{residentId}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public ResponseEntity<List<RequestResource>> getRequestsByResident(@PathVariable Long residentId) {
        var requests = requestQueryService.handle(new GetAllRequestsByResidentIdQuery(residentId));
        var resources = requests.stream()
                .map(RequestResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/provider/{providerId}")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ResponseEntity<List<RequestResource>> getRequestsByProvider(@PathVariable Long providerId) {
        var requests = requestQueryService.handle(new GetAllRequestsByProviderIdQuery(providerId));
        var resources = requests.stream()
                .map(RequestResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }
}