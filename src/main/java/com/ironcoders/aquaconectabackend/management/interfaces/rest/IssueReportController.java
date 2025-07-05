package com.ironcoders.aquaconectabackend.management.interfaces.rest;
import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.management.domain.model.aggregates.IssueReport;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.CreateIssueReportCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.commads.UpdateIssueReportCommand;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetAllIssueReportsQuery;
import com.ironcoders.aquaconectabackend.management.domain.model.queries.GetIssueReportByIdQuery;
import com.ironcoders.aquaconectabackend.management.domain.services.IssueReportCommandService;
import com.ironcoders.aquaconectabackend.management.domain.services.IssueReportQueryService;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.CreateIssueReportResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.IssueReportResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.resources.UpdateIssueReportResource;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.CreateIssueReportCommandFromResourceAssembler;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.IssueReportResourceFromEntityAssembler;
import com.ironcoders.aquaconectabackend.management.interfaces.rest.transform.UpdateIssueReportCommandFromResource;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/requests")
@Tag(name = "Requests", description = "Request Management endpoints")
@PreAuthorize("isAuthenticated()")
public class IssueReportController {

    private final IssueReportCommandService requestCommandService;
    private final IssueReportQueryService requestQueryService;

    public IssueReportController(IssueReportCommandService requestCommandService, IssueReportQueryService requestQueryService) {
        this.requestCommandService = requestCommandService;
        this.requestQueryService = requestQueryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<IssueReportResource> createRequest(@RequestBody CreateIssueReportResource resource) throws AccessDeniedException {
        // Get the user ID from the security context

          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();


        CreateIssueReportCommand createRequestCommand = CreateIssueReportCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var request = requestCommandService.handle(createRequestCommand);
        if (request.isEmpty())  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var requestResource = IssueReportResourceFromEntityAssembler.toResourceFromEntity(request.get());
        return new ResponseEntity<>(requestResource, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ResponseEntity<IssueReportResource> updateRequest(
            @PathVariable Long id,
            @RequestBody UpdateIssueReportResource resource) throws AccessDeniedException {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        UpdateIssueReportCommand command = UpdateIssueReportCommandFromResource.toCommandFromResource(id, resource, userId);

        Optional<IssueReport> result = requestCommandService.handle(command);

        return result
                .map(IssueReportResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public ResponseEntity<IssueReportResource> getRequestById(@PathVariable Long id) {
        return requestQueryService.handle(new GetIssueReportByIdQuery(id))
                .map(IssueReportResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }




    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<IssueReport>> getAllRequests() {
        List<IssueReport> requests = requestQueryService.handle(new GetAllIssueReportsQuery());

        if (requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(requests);
    }










}