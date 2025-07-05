package com.ironcoders.aquaconectabackend.profiles.interfaces.rest;

// import com.fasterxml.jackson.databind.introspect.AccessorNamingStrategy.Provider;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Provider;
import com.ironcoders.aquaconectabackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.ironcoders.aquaconectabackend.iam.interfaces.acl.IamContextFacade;
import com.ironcoders.aquaconectabackend.profiles.domain.model.DTO.ResidentWithCredentials;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Profile;
import com.ironcoders.aquaconectabackend.profiles.domain.model.aggregates.Resident;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.CreateResidentCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.commands.UpdateResidentCommand;
import com.ironcoders.aquaconectabackend.profiles.domain.model.queries.GetResidentsByProviderIdQuery;
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

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    private final ProfileRepository profileRepository;
   IamContextFacade iamContextFacade;

    public ResidentController(
            ResidentCommandService residentCommandService,
            ResidentQueryService residentQueryService,
            ResidentRepository residentRepository,
            ProviderQueryService providerQueryService,
            ProfileRepository profileRepository,
            IamContextFacade iamContextFacade) {
        this.residentCommandService = residentCommandService;
        this.residentQueryService = residentQueryService;
        this.residentRepository = residentRepository;
        this.providerQueryService = providerQueryService;
        this.profileRepository = profileRepository;
        this.iamContextFacade = iamContextFacade;
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

        // 3. Find the resident's profile using the userId
        List<Profile> profiles = profileRepository.findByUserId(result.resident().getUserId());

        // 4. Convert the result to a resource, including generated username and password
        ResidentResource residentResource = ResidentResourceFromEntityAssembler.toResourceFromEntityWithCredentials(
                result.resident(),
                result.username(),
                result.password(),
                profiles.get(0)
        );

        return new ResponseEntity<>(residentResource, HttpStatus.CREATED);
    }


    // @GetMapping("/{id}/sensors")
    // @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    // public List<SensorResource> getAllSensorsByResidentId(@PathVariable Long residentId) {
    //     return sensorQueryService.handle(new GetAllSensorsByResidentId(residentId))
    //             .stream()
    //             .map(SensorResourceFromEntityAssembler::toResourceFromEntity)
    //             .collect(Collectors.toList());
    // }

    // @GetMapping("/{id}/subscriptions")
    // @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_RESIDENT')")
    // public ResponseEntity<List<SubscriptionResource>> getSubscriptionsByResidentId(@PathVariable Long id) throws AccessDeniedException {

    //     var query = new GetAllSubscriptionsByResidentId(id);
    //     var subscriptions = subscriptionQueryService.handle(query);

    //     if (subscriptions.isEmpty()) return ResponseEntity.notFound().build();

    //     var subscriptionResources = subscriptions.stream()
    //             .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
    //             .toList();

    //     return new ResponseEntity<>(subscriptionResources, HttpStatus.OK);
    // }

    // @GetMapping("/{id}/requests")
    // @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    // public ResponseEntity<List<RequestResource>> getRequestsByResident(@PathVariable Long residentId) {
    //     var requests = requestQueryService.handle(new GetAllRequestsByResidentIdQuery(residentId));
    //     var resources = requests.stream()
    //             .map(RequestResourceFromEntityAssembler::toResourceFromEntity)
    //             .collect(Collectors.toList());
    //     return ResponseEntity.ok(resources);
    // }

 

    // @GetMapping("/{id}/water-requests")
    // @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_RESIDENT')")
    // public List<WaterRequestResource> getWaterRequestsByResident(@PathVariable Long residentId) {
    //     return waterRequestQueryService.handle(new GetWaterRequestsByResidentIdQuery(residentId))
    //             .stream()
    //             .map(WaterRequestResourceFromAggregateAssembler::toResourceFromEntity)
    //             .collect(Collectors.toList());
    // }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ResponseEntity<List<ResidentResource>> getResidentsForAuthenticatedProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId = userDetails.getId();

        // Buscar el proveedor por su userId
        Optional<Provider> providerOptional = providerQueryService.findByUserId(userId);
        if (providerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // o 404 si prefieres
        }

        Long providerId = providerOptional.get().getId();

        // Consultar residentes
        var query = new GetResidentsByProviderIdQuery(providerId);
        var residents = residentQueryService.handle(query);
        if (residents.isEmpty()) return ResponseEntity.notFound().build();

        var residentResources = residents.stream().map(resident -> {

            List<Profile> profiles = profileRepository.findByUserId(resident.getUserId());
            String username = iamContextFacade.fetchUsernameByUserId(resident.getUserId());
            return ResidentResourceFromEntityAssembler.toResourceFromEntityWithCredentials(resident, username, null,profiles.get(0));
        }).toList();

        return ResponseEntity.ok(residentResources);
    }


    // @GetMapping("/me")
    // @PreAuthorize("hasRole('ROLE_RESIDENT')")
    // public ResponseEntity<ResidentResource> getAuthenticatedResident() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    //     long userId = userDetails.getId();

    //     // Buscar residente por su userId
    //     List<Resident> residentOptional = residentQueryService.findByUserId (userId);
    //     if (residentOptional.isEmpty()) {
    //         return ResponseEntity.notFound().build();
    //     }

    //     Resident resident = residentOptional.get(0);
    //     String username = iamContextFacade.fetchUsernameByUserId(userId);
    //     List<Profile> profiles = profileRepository.findByUserId(resident.getUserId());

    //     ResidentResource resource = ResidentResourceFromEntityAssembler
    //             .toResourceFromEntityWithCredentials(resident, username, null,profiles.get(0));

    //     return ResponseEntity.ok(resource);
    // }


    // @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ROLE_PROVIDER') or hasRole('ROLE_ADMIN')")
    // public ResponseEntity<List<ResidentResource>> getResidentsByUserId(@RequestParam Long userId) {
    //     var query = new GetResidentByUserIdQuery(userId);
    //     var residents = residentQueryService.handle(query);
    //     if (residents.isEmpty()) return ResponseEntity.notFound().build();

    //     // Obtener username para cada userId del residente
    //     var residentResources = residents.stream().map(resident -> {
    //         String username = iamContextFacade.fetchUsernameByUserId(resident.getUserId());
    //         List<Profile> profiles = profileRepository.findByUserId(resident.getUserId());
    //         return ResidentResourceFromEntityAssembler.toResourceFromEntityWithCredentials(resident, username, null,profiles.get(0));
    //     }).toList();

    //     return ResponseEntity.ok(residentResources);
    // }


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
        List<Profile> profiles = profileRepository.findByUserId(updatedResident.getUserId());

        ResidentResource residentResource = ResidentResourceFromEntityAssembler.toResourceFromEntity(updatedResident, profiles.get(0));

        return ResponseEntity.ok(residentResource);
    }

}
