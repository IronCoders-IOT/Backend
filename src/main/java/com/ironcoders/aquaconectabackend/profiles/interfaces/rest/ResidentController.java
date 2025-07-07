package com.ironcoders.aquaconectabackend.profiles.interfaces.rest;

// import com.fasterxml.jackson.databind.introspect.AccessorNamingStrategy.Provider;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.iam.interfaces.acl.IamContextFacade;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.aggregates.IssueReport;
import com.ironcoders.aquaconectabackend.monitoring.domain.model.queries.GetAllDevicesByResidentId;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.acl.DeviceContextFacade;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.acl.IssueReportContextFacade;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.acl.WaterSupplyRequestContextFacade;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources.DeviceResource;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.resources.WaterSupplyRequestResource;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import com.ironcoders.aquaconectabackend.monitoring.interfaces.rest.transform.WaterRequestResourceFromAggregateAssembler;
import com.ironcoders.aquaconectabackend.profiles.domain.model.DTO.ResidentWithCredentials;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.UpdateResidentCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.queries.GetAllResidentsQuery;
import com.ironcoders.aquaconectabackend.profiles.domain.model.queries.GetProfileByUserIdQuery;
import com.ironcoders.aquaconectabackend.profiles.domain.model.queries.GetResidentsByProviderIdQuery;
import com.ironcoders.aquaconectabackend.profiles.domain.model.queries.GetWaterRequestsByResidentIdQuery;
import com.ironcoders.aquaconectabackend.profiles.domain.services.ProfileQueryService;
import com.ironcoders.aquaconectabackend.profiles.domain.services.ResidentCommandService;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ProviderQueryService;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ResidentQueryService;
import com.ironcoders.aquaconectabackend.profiles.infrastructure.persistence.jpa.repositories.ResidentRepository;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.resources.CreateResidentResource;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.resources.ResidentResource;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.resources.UpdateResidentResource;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.transform.CreateResidentCommandFromResourceAssembler;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.transform.ResidentResourceFromEntityAssembler;
import com.ironcoders.aquaconectabackend.profiles.interfaces.rest.transform.UpdateResidentCommandFromResource;
import com.ironcoders.aquaconectabackend.subcriptions.domain.model.queries.GetAllSubscriptionsByResidentId;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.acl.SubscriptionContextFacade;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.resources.SubscriptionResource;
import com.ironcoders.aquaconectabackend.subcriptions.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/residents", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Residents", description = "Resident Management Endpoints")
@PreAuthorize("isAuthenticated()")
public class ResidentController {

    private final ResidentCommandService residentCommandService;
    private final ResidentQueryService residentQueryService;
    private final ResidentRepository residentRepository;
    private final ProviderQueryService providerQueryService;
    private final ProfileQueryService profileQueryService;

   IamContextFacade iamContextFacade;
   WaterSupplyRequestContextFacade waterSupplyRequestContextFacade;
   IssueReportContextFacade issueReportContextFacade;
   DeviceContextFacade  deviceContextFacade;
   SubscriptionContextFacade subscriptionContextFacade;

    public ResidentController(
            ResidentCommandService residentCommandService,
            ResidentQueryService residentQueryService,
            ResidentRepository residentRepository,
            ProviderQueryService providerQueryService,
            ProfileRepository profileRepository,
            IamContextFacade iamContextFacade,
            WaterSupplyRequestContextFacade waterSupplyRequestContextFacade,
            ProfileQueryService profileQueryService,
            IssueReportContextFacade issueReportContextFacade,
            DeviceContextFacade deviceContextFacade,
            SubscriptionContextFacade subscriptionContextFacade
            ) {
        this.residentCommandService = residentCommandService;
        this.residentQueryService = residentQueryService;
        this.residentRepository = residentRepository;
        this.providerQueryService = providerQueryService;
        this.iamContextFacade = iamContextFacade;
        this.waterSupplyRequestContextFacade = waterSupplyRequestContextFacade;
        this.profileQueryService = profileQueryService;
        this.issueReportContextFacade = issueReportContextFacade;
        this.deviceContextFacade = deviceContextFacade;
        this.subscriptionContextFacade = subscriptionContextFacade;
    }

    @PostMapping
@PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
public ResponseEntity<ResidentResource> createResident(@RequestBody CreateResidentResource resource) throws AccessDeniedException {
    // 0. Get authenticated user id
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getId();

    // 1. Convert the resource to a command, passing the authenticated user id
    CreateResidentCommand command = CreateResidentCommandFromResourceAssembler.toCommandFromResource(resource, userId);
    // (O ajusta el assembler para que reciba el userId y lo ponga en el command)

    // 2. Execute the use case
    ResidentWithCredentials result = residentCommandService.handle(command);

    // 3. Find the resident's profile using the NEW userId (not resource.userId())
    Long newUserId = result.resident().getUserId();
    Optional<Profile> profiles = profileQueryService.handle(new GetProfileByUserIdQuery(newUserId));
    if (profiles.isEmpty()) {
        throw new IllegalStateException("Profile for the new resident could not be found.");
    }

    // 4. Convert the result to a resource, including generated username and password
    ResidentResource residentResource = ResidentResourceFromEntityAssembler.toResourceFromEntityWithCredentials(
            result.resident(),
            result.username(),
            result.password(),
            profiles.get()
    );

    return new ResponseEntity<>(residentResource, HttpStatus.CREATED);
}


    @GetMapping("/{residentId}/devices")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public List<DeviceResource> getAllDevicesByResidentId(@PathVariable Long residentId) {
        return deviceContextFacade.getAllDevicesByResidentId(residentId)
                .stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
    }

        @GetMapping("/{id}/subscriptions")
        @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_RESIDENT')")
        public ResponseEntity<List<SubscriptionResource>> getSubscriptionsByResidentId(@PathVariable Long id) throws AccessDeniedException {

            var subscriptions = subscriptionContextFacade.fetchSubscriptionsByResidentId(id);

            if (subscriptions.isEmpty()) return ResponseEntity.notFound().build();

            var subscriptionResources = subscriptions.stream()
                    .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();

            return new ResponseEntity<>(subscriptionResources, HttpStatus.OK);
        }

    @GetMapping("/{residentId}/issue-reports")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    public ResponseEntity<List<IssueReport>> getRequestsByResidentId(@PathVariable Long residentId) {
        var requests = issueReportContextFacade.fetchIssueReportsByResidentId(residentId)
                .stream()
                .filter(request -> request.getResidentId().equals(residentId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{residentId}/water-supply-requests")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public List<WaterSupplyRequestResource> getWaterRequestsByResident(@PathVariable Long residentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();


        final Long providerId;

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROVIDER"))) {
            Optional<Provider> providerOptional = providerQueryService.findByUserId(userId);

            if (providerOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provider not found");
            }

            providerId = providerOptional.get().getId();

            List<Resident> residents = residentQueryService.handle(new GetResidentsByProviderIdQuery(providerId));


            boolean isResidentValid = residents.stream().anyMatch(resident -> resident.getId().equals(residentId));

            if (!isResidentValid) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Resident does not belong to this provider");
            }
        } else {
            providerId = null;
        }


        List<WaterSupplyRequestResource> result = waterSupplyRequestContextFacade.fetchWaterRequestsByResidentId(residentId)
                .stream()
                .filter(resource -> providerId == null || resource.getProviderId().equals(providerId))
                .map(WaterRequestResourceFromAggregateAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return result;
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ResidentResource>> getResidentsForAuthenticatedProviderOrAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Resident> residents;

        if (isAdmin) {
            // Si es admin, obtener todos los residentes
            residents = residentQueryService.handle(new GetAllResidentsQuery());
        } else {
            // Buscar el proveedor por su userId
            Optional<Provider> providerOptional = providerQueryService.findByUserId(userId);
            if (providerOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            Long providerId = providerOptional.get().getId();

            // Consultar residentes del proveedor
            residents = residentQueryService.handle(new GetResidentsByProviderIdQuery(providerId));
        }

        if (residents.isEmpty()) return ResponseEntity.notFound().build();

        List<ResidentResource> residentResources = residents.stream()
            .map(resident -> {
                Optional<Profile> profileOptional = profileQueryService.handle(new GetProfileByUserIdQuery(resident.getUserId()));
                String username = iamContextFacade.fetchUsernameByUserId(resident.getUserId());
                return profileOptional
                    .map(profile -> ResidentResourceFromEntityAssembler.toResourceFromEntityWithCredentials(resident, username, null, profile))
                    .orElse(null);
            })
            .filter(resource -> resource != null)
            .collect(Collectors.toList());

        return ResponseEntity.ok(residentResources);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ResidentResource> getAuthenticatedResident() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        List<Resident> residentOptional = residentQueryService.findByUserId (userId);
        if (residentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Resident resident = residentOptional.get(0);
        String username = iamContextFacade.fetchUsernameByUserId(userId);
        List<Profile> profiles = profileQueryService.handle(new GetProfileByUserIdQuery(userId))
                .stream()
                .collect(Collectors.toList());
        ResidentResource resource = ResidentResourceFromEntityAssembler
                .toResourceFromEntityWithCredentials(resident, username, null,profiles.get(0));

        return ResponseEntity.ok(resource);
    }


    @GetMapping("/{residentId}")
    @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ResidentResource>> getResidentById(@PathVariable Long residentId) {


        Optional<Resident> residentOptional = residentQueryService.findById(residentId);
        if (residentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Resident resident = residentOptional.get();

        Optional<Profile> profiles = profileQueryService.handle(new GetProfileByUserIdQuery(resident.getUserId()));
        if (profiles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ResidentResource residentResource = ResidentResourceFromEntityAssembler.toResourceFromEntity(resident, profiles.get());

        return ResponseEntity.ok(List.of(residentResource));


    }


    @PutMapping("/me")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ResidentResource> updateResident(@RequestBody UpdateResidentResource resource) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        UpdateResidentCommand updateResidentCommand = UpdateResidentCommandFromResource.toCommandFromResource(resource, userId);

        Optional<Resident> updatedResidentOptional = residentCommandService.handle(updateResidentCommand);

        if (updatedResidentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Resident updatedResident = updatedResidentOptional.get();
        List<Profile> profiles = profileQueryService.handle(new GetProfileByUserIdQuery(updatedResident.getUserId()))
                .stream()
                .collect(Collectors.toList());

        ResidentResource residentResource = ResidentResourceFromEntityAssembler.toResourceFromEntity(updatedResident, profiles.get(0));

        return ResponseEntity.ok(residentResource);
    }

}
